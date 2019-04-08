package com.suhotrub.conversations.ui.util.recycler.item;


import com.suhotrub.conversations.ui.util.recycler.controller.StickyBindableItemController;
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder;

public final class StickyBindableItem<T, H extends BindableViewHolder<T>> extends BaseItem<H> implements StickyHeader {
    private T data;

    public StickyBindableItem(T data, StickyBindableItemController<T, H> itemController) {
        super(itemController);
        this.data = data;
    }

    public T getData() {
        return data;
    }

}