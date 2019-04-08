package com.suhotrub.conversations.ui.util.ui

import android.view.View


fun View.setVisibleOrGone(isVisibile: Boolean) {
    this.visibility =
            if (isVisibile) View.VISIBLE
            else View.GONE
}