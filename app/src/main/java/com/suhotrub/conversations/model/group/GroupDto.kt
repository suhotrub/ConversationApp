package com.suhotrub.conversations.model.group

import android.os.Parcelable
import com.suhotrub.conversations.model.messages.MessageDto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupDto(
        //val id: Long,
        val groupGuid: String = "",
        val name: String? = null,
        val description: String? = null,
        val lastMessage: MessageDto? = null,
        val inCall: Boolean? = null
        //val message:String,
        //val avatar:Int,
        //val statusText:String,
        //val statusType: ChatStatusType,
        //val time: Long
) : Parcelable

