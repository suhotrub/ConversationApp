package com.suhotrub.conversations.ui.activities.groupinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.Window
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.model.user.UsersDto
import com.suhotrub.conversations.ui.util.recycler.EasyAdapter
import com.suhotrub.conversations.ui.util.recycler.ItemList
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import com.suhotrub.conversations.ui.util.widget.user.UserItemController
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_group_info.*
import javax.inject.Inject


class GroupInfoActivity : MvpAppCompatActivity(), GroupInfoView {

    @Inject
    @InjectPresenter
    lateinit var presenter: GroupInfoPresenter

    @ProvidePresenter
    fun providePresenter() = presenter


    val userItemController = UserItemController({})

    val adapter = EasyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        val fade = Fade()
        fade.excludeTarget(R.id.appbar, true)
        fade.excludeTarget(R.id.toolbar, true)
        window.enterTransition = fade
        window.exitTransition = fade

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        StatusBarUtil.setTranslucentForCoordinatorLayout(this, ResourcesCompat.getColor(resources, R.color.colorPrimary, null))


        setContentView(R.layout.activity_group_info)

        create_group_users_rv.adapter = adapter
        create_group_users_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun renderGroup(groupDto: GroupDto) {
        group_title_tv.setTextOrGone(groupDto.name)
        description.setTextOrGone(groupDto.description)
    }

    override fun renderUsers(users: List<UserDto>) {
        adapter.setItems(
                ItemList.create()
                        .addAll(users, userItemController)
        )
        group_users_count_tv.setTextOrGone("${users.size} долбаебов")
    }

    companion object {
        fun prepareIntent(ctx: Context, groupDto: GroupDto) =
                Intent(ctx, GroupInfoActivity::class.java).putExtra("EXTRA_FIRST", groupDto)
    }
}