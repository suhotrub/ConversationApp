package com.suhotrub.conversations.ui.util.recycler.controller;


import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder;
import com.suhotrub.conversations.ui.util.recycler.item.StickyBindableItem;

public abstract class StickyBindableItemController<T, H extends BindableViewHolder<T>>
        extends com.suhotrub.conversations.ui.util.recycler.controller.BaseItemController<H, StickyBindableItem<T, H>> {

    @Override
    public final void bind(H holder, StickyBindableItem<T, H> item) {
        bind(holder, item.getData());
    }

    public void bind(H holder, T data) {
        holder.bind(data);
    }

    @Override
    public final long getItemId(StickyBindableItem<T, H> item) {
        return getItemId(item.getData());
    }

    @Override
    public final long getItemHash(StickyBindableItem<T, H> item) {
        return getItemHash(item.getData());
    }

    public long getItemId(T data) {
        return NO_ID;
    }

    public long getItemHash(T data) {
        return data.hashCode();
    }
}