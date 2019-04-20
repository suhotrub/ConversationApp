package com.suhotrub.conversations.model.messages

import android.os.Parcelable
import com.suhotrub.conversations.model.user.UserDto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageDto(
        val text: String?,
        val groupGuid: String?,
        val groupName:String?,
        val time: String?,
        val user: UserDto?
) : Parcelable