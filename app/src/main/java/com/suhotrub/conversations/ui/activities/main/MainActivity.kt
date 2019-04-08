package com.suhotrub.conversations.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.Chat
import com.suhotrub.conversations.ui.activities.logout.LogoutActivity
import com.suhotrub.conversations.ui.activities.main.recycler.ChatItemController
import com.suhotrub.conversations.ui.util.recycler.EasyAdapter
import com.suhotrub.conversations.ui.util.recycler.ItemList
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    val adapter = EasyAdapter()
    val chatItemController = ChatItemController()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        main_contacts_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_contacts_rv.adapter = adapter

        main_toolbar.inflateMenu(R.menu.main_menu)
        main_toolbar.overflowIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_more_vert_white_transparent_24dp, null)

        main_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout ->{
                    startActivity(Intent(this@MainActivity, LogoutActivity::class.java))
                }
                else -> {
                }
            }
            true
        }
    }

    override fun renderChats(chats: List<Chat>) {
        adapter.setItems(
                ItemList.create()
                        .addAll(chats, chatItemController)
        )
    }
}