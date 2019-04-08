package com.suhotrub.conversations.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDto(
        val name: String? = null,
        val login: String? = null,
        val surname: String? = null
) : Parcelable