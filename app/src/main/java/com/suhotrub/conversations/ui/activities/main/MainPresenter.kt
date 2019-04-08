package com.suhotrub.conversations.ui.activities.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.Chat
import com.suhotrub.conversations.model.ChatStatusType
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
) : MvpPresenter<MainView>() {

    init {
        reload()
    }

    fun reload(){



        viewState.renderChats(listOf(
                Chat(
                        id = 0,
                        name = "Gorbachev Alexey",
                        statusType = ChatStatusType.ONLINE,
                        statusText = "Poshel nahuy",
                        time = Date().time,
                        message = "Я роняю запад У",
                        avatar = R.drawable.bg_circle_red
                ),
                Chat(
                        id = 1,
                        name = "Gorbachev asdasd asd asd asd asd asd  Alexey",
                        statusType = ChatStatusType.OFFLINE,
                        statusText = "Poshel asdasd asdasdasdasdsd  nahuy",
                        time = Date().time,
                        message = "Я роняю запад У",
                        avatar = R.drawable.bg_circle_red
                ),
                Chat(
                        id = 2,
                        name = "Gorbachev Alexey",
                        statusType = ChatStatusType.ONLINE,
                        statusText = "Poshel nahuy",
                        time = Date().time,
                        message = "Я роняю запад У",
                        avatar = R.drawable.bg_circle_red
                ),
                Chat(
                        id = 3,
                        name = "Gorbachev asdasdasdasdasdasdasdasdasdasd Alexey",
                        statusType = ChatStatusType.ONLINE,
                        statusText = "Poshel asdasdasdasdasdasdasdasdasdasd nahuy",
                        time = Date().time,
                        message = "Я роняю asdasdasdasdasdasdasdasdasd запад У",
                        avatar = R.drawable.bg_circle_red
                )
        ))
    }

}