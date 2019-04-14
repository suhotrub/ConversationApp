package com.suhotrub.conversations.ui.activities.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.view.View
import android.view.Window
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jaeger.library.StatusBarUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import com.suhotrub.conversations.R
import com.suhotrub.conversations.interactor.user.UsersRepository
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.activities.call.CallActivity
import com.suhotrub.conversations.ui.activities.groupinfo.GroupInfoActivity
import com.suhotrub.conversations.ui.util.recycler.ItemList
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.recycler.PaginationableAdapter
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import com.suhotrub.conversations.ui.util.ui.showError
import com.suhotrub.conversations.ui.util.widget.message.MessageItemController
import com.suhotrub.conversations.ui.util.widget.message.SentMessageItemController
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_group.*
import javax.inject.Inject

class GroupActivity : MvpAppCompatActivity(), GroupActivityView {


    @Inject
    @InjectPresenter
    lateinit var presenter: GroupPresenter

    @Inject
    lateinit var usersRepository: UsersRepository

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter = PaginationableAdapter()
    private val messageItemController = MessageItemController({})
    private val sentMessageItemController = SentMessageItemController({})

    var canScroll = true

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground,true)
        fade.excludeTarget(R.id.appbar, true)
        fade.excludeTarget(R.id.toolbar, true)

        window.enterTransition = fade
        window.exitTransition = fade

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        setContentView(R.layout.activity_group)
        //findViewById<View>(android.R.id.statusBarBackground)?.elevation = appbar.elevation

        group_users_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true).apply { stackFromEnd = true }
        group_users_rv.adapter = adapter

        group_users_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                canScroll = ((group_users_rv.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                        ?: 0) < 2

            }
        })

        adapter.setOnShowMoreListener {
            adapter.setState(PaginationState.LOADING)
            presenter.loadMore()
        }


        group_header.setOnClickListener {
            val intent = GroupInfoActivity.prepareIntent(this, intent.getParcelableExtra<GroupDto>("EXTRA_FIRST")
                    ?: GroupDto())
            startActivity(intent,
                    ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    this,
                                    Pair.create(appbar as View, "appbar"),
                                    Pair.create(group_title_tv as View, "group_title_tv"),
                                    Pair.create(group_users_count_tv as View, "group_users_count_tv")
                            ).toBundle()
            )
        }

        adapter.setState(PaginationState.READY)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        presenter.observeMessage(RxTextView.textChanges(message_et).map(CharSequence::toString))
        send_message_btn.setOnClickListener {
            presenter.sendMessage()
        }

        group_call_btn.setOnClickListener {
            startActivity(CallActivity.prepareIntent(this@GroupActivity,groupDto))
        }
    }
    lateinit var groupDto: GroupDto
    override fun renderGroup(groupDto: GroupDto) {
        this.groupDto = groupDto
        group_title_tv.setTextOrGone(groupDto.name)
    }

    override fun showErrorSnackbar(t: Throwable) =
            showError(t)

    override fun renderMessages(messages: List<MessageDto>) {
        adapter.setItems(
                ItemList.create()
                        .apply {
                            messages.forEach {
                                add(it,
                                        if (it.user.login != usersRepository.getCurrentUser()?.login)
                                            messageItemController
                                        else
                                            sentMessageItemController
                                )
                            }
                        }
        )

        //group_users_count_tv.setTextOrGone("${users.size} долбаёбов")
        adapter.setState(PaginationState.READY)
    }

    override fun clearMessage() {
        message_et.text = null
    }

    override fun scrollToBottom() {
        if (canScroll) {
            group_users_rv.smoothScrollToPosition(0)
        }
    }

    override fun setPaginationState(paginationState: PaginationState) =
            adapter.setState(paginationState)

    companion object {
        fun prepareIntent(ctx: Context, groupDto: GroupDto) =
                Intent(ctx, GroupActivity::class.java).putExtra("EXTRA_FIRST", groupDto)
    }
}