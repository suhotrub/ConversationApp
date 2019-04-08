package com.suhotrub.conversations.ui.activities.auth.signin

import com.suhotrub.conversations.ui.util.recycler.LoadState

data class SignInScreenModel(
        var login:String? = null,
        var password:String? = null,
        var loadState: LoadState = LoadState.NONE
)