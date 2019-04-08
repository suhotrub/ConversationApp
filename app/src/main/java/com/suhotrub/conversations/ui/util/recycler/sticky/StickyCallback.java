package com.suhotrub.conversations.ui.util.recycler.sticky;

import android.support.annotation.Nullable;

public interface StickyCallback<T> {
    T onSticky(@Nullable Object prev, Object next);
}