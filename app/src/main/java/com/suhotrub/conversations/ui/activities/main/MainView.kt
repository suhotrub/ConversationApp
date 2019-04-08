package com.suhotrub.conversations.ui.activities.main

import com.arellomobile.mvp.MvpView
import com.suhotrub.conversations.model.group.GroupDto

interface MainView: MvpView{
    fun renderChats(list:List<GroupDto>)

}