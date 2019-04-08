package com.suhotrub.conversations.ui.util.recycler.controller;

import com.suhotrub.conversations.ui.util.recycler.holder.CheckableViewHolder;

public abstract class CheckableItemController<T1, H extends CheckableViewHolder<T1>>
        extends BindableItemController<T1, H> {

    private Long checkedItemId = -1L;

    protected OnItemCheckedListener checkedControllerCallback;

    public interface OnItemCheckedListener {
        void onItemChecked(long checkedItemId, CheckableItemController controller);
    }

    public void setOnCheckItemListener(OnItemCheckedListener checkedControllerCallback) {
        this.checkedControllerCallback = checkedControllerCallback;
    }

    public Long getCheckedItemId() {
        return checkedItemId;
    }

    public void setCheckedItemId(Long selectedItemId) {
        this.checkedItemId = selectedItemId;
    }
}