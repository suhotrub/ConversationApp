package com.suhotrub.conversations.model.webrtc

import android.os.Parcelable
import com.suhotrub.conversations.model.group.GroupDto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IncomingCallDto(val group: GroupDto) : Parcelable