package com.suhotrub.conversations.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.activities.creategroup.CreateGroupActivity
import com.suhotrub.conversations.ui.activities.group.GroupActivity
import com.suhotrub.conversations.ui.activities.logout.LogoutActivity
import com.suhotrub.conversations.ui.activities.main.recycler.GroupItemController
import com.suhotrub.conversations.ui.util.recycler.ItemList
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.recycler.PaginationableAdapter
import com.suhotrub.conversations.ui.util.ui.showError
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter = PaginationableAdapter()
    private val chatItemController = GroupItemController {
        startActivity(GroupActivity.prepareIntent(this@MainActivity, it))
    }

    override fun onResume() {
        super.onResume()
        presenter.reload()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        main_contacts_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_contacts_rv.adapter = adapter

        adapter.setOnShowMoreListener {
            adapter.setState(PaginationState.LOADING)
            presenter.loadMore()
        }
        main_fab_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateGroupActivity::class.java))
        }

        adapter.setState(PaginationState.READY)
        main_toolbar.inflateMenu(R.menu.main_menu)
        main_toolbar.overflowIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_more_vert_white_transparent_24dp, null)

        main_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    startActivity(Intent(this@MainActivity, LogoutActivity::class.java))
                }
                else -> {
                }
            }
            true
        }
    }

    override fun showErrorSnackbar(t: Throwable) =
            showError(t)

    override fun renderChats(chats: List<GroupDto>) {
        adapter.setItems(
                ItemList.create()
                        .addAll(chats, chatItemController)
        )
        adapter.setState(PaginationState.READY)
    }

    override fun setPaginationState(paginationState: PaginationState) =
            adapter.setState(paginationState)
}



