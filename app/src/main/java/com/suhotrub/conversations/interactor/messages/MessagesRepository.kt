package com.suhotrub.conversations.interactor.messages

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagesRepository @Inject constructor(
        val messagesApi: MessagesApi
) {

    fun getGroupMessages(groupGuid: String, offset: Int) =
            messagesApi.getGroupMessages(groupGuid, offset, 20)

}