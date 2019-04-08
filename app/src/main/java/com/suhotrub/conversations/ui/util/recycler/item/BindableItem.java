package com.suhotrub.conversations.ui.util.recycler.item;


import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController;
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder;

public class BindableItem<T, H extends BindableViewHolder<T>> extends BaseItem<H> {
    private T data;

    public BindableItem(T data, BindableItemController<T, H> itemController) {
        super(itemController);
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
