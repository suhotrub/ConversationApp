package com.suhotrub.conversations.model.group

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupDto(
        //val id: Long,
        val groupGuid: String = "",
        val name: String? = null,
        val description: String? = null
        //val message:String,
        //val avatar:Int,
        //val statusText:String,
        //val statusType: ChatStatusType,
        //val time: Long
) : Parcelable

