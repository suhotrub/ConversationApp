package com.suhotrub.conversations.ui.activities.auth.signup

import com.suhotrub.conversations.ui.util.recycler.LoadState

data class SignUpScreenModel(
        var login: String? = null,
        var password: String? = null,
        var passwordAgain: String? = null,
        var name: String? = null,
        var surname: String? = null,
        var loadState: LoadState = LoadState.NONE
)