package com.suhotrub.conversations.ui.util.recycler.holder;

import android.view.View;
import android.view.ViewGroup;

/**
 * ViewHolder поддерживающий одиночное выделения элемента
 */
public abstract class CheckableViewHolder<T1> extends BindableViewHolder<T1> {

    public CheckableViewHolder(ViewGroup parent, int layoutRes) {
        super(parent, layoutRes);
    }

    public CheckableViewHolder(View itemView) {
        super(itemView);
    }

    public abstract boolean isChecked();
}