package com.suhotrub.conversations.ui.util.recycler;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController;
import com.suhotrub.conversations.ui.util.recycler.controller.NoDataItemController;
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder;
import com.suhotrub.conversations.ui.util.recycler.item.BaseItem;
import com.suhotrub.conversations.ui.util.recycler.item.BindableItem;
import com.suhotrub.conversations.ui.util.recycler.item.NoDataItem;
import com.suhotrub.conversations.ui.util.recycler.item.StickyBindableItem;

public class ItemList extends ArrayList<BaseItem> {

    public interface BindableItemControllerProvider<T> {
        BindableItemController<T, ? extends BindableViewHolder<T>> provide(T data);
    }

    public ItemList(int initialCapacity) {
        super(initialCapacity);
    }

    public ItemList(Collection<? extends BaseItem> items) {
        super(items);
    }

    public ItemList() {
    }

    public static ItemList create() {
        return new ItemList();
    }

    public static ItemList create(Collection<BaseItem> items) {
        return new ItemList(items);
    }

    public static <T> ItemList create(Collection<T> data,
                                      BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return create(data, itemData -> itemController);
    }

    public static <T> ItemList create(Collection<T> data,
                                      BindableItemControllerProvider<T> itemControllerProvider) {
        ItemList items = new ItemList(data.size());
        for (T dataItem : data) {
            items.addItem(new BindableItem<>(dataItem, itemControllerProvider.provide(dataItem)));
        }
        return items;
    }

    //single insert

    public ItemList insert(int index, BaseItem item) {
        this.add(index, item);
        return this;
    }

    //single add

    public ItemList addItem(BaseItem item) {
        return insert(this.size(), item);
    }

    public <T> ItemList add(T data,
                            BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new BindableItem<>(data, itemController));
    }


    //collection insert

    public ItemList insertAll(int index, Collection<BaseItem> items) {
        this.addAll(index, items);
        return this;
    }
    public <T> ItemList insertAll(int index, Collection<T> data, BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAll(index, data, dataItem -> itemController);
    }

    public <T> ItemList addAll(Collection<T> data,
                               BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAll(this.size(), data, itemController);
    }

    public <T> ItemList insertAll(int index,
                                  Collection<T> data,
                                  BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAll(index, create(data, itemControllerProvider));
    }

    // add footer

    public ItemList addFooter(BaseItem item) {
        return insert(this.size(), item);
    }

    public ItemList addFooter(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new NoDataItem<>(itemController));
    }

}
