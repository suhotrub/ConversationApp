package com.suhotrub.conversations.ui.activities.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.recycler.ItemList
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.recycler.PaginationableAdapter
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import com.suhotrub.conversations.ui.util.ui.showError
import com.suhotrub.conversations.ui.util.widget.user.UserItemController
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_group.*
import retrofit2.HttpException
import javax.inject.Inject

class GroupActivity : MvpAppCompatActivity(), GroupActivityView {


    @Inject
    @InjectPresenter
    lateinit var presenter: GroupPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter = PaginationableAdapter()
    private val userItemController = UserItemController({})

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group)

        group_users_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        group_users_rv.adapter = adapter

        adapter.setOnShowMoreListener {
            adapter.setState(PaginationState.LOADING)
            presenter.loadMore()
        }


        adapter.setState(PaginationState.READY)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun renderGroup(groupDto: GroupDto) {
        group_title_tv.setTextOrGone(groupDto.name)
    }

    override fun showErrorSnackbar(t: Throwable) =
            showError(t)

    override fun renderUsers(users: List<UserDto>) {
        adapter.setItems(
                ItemList.create()
                        .addAll(users, userItemController)
        )
        group_users_count_tv.setTextOrGone("${users.size} долбаёбов")
        adapter.setState(PaginationState.READY)
    }

    override fun setPaginationState(paginationState: PaginationState) =
            adapter.setState(paginationState)

    companion object {
        fun prepareIntent(ctx: Context, groupDto: GroupDto) =
                Intent(ctx, GroupActivity::class.java).putExtra("EXTRA_FIRST", groupDto)
    }
}