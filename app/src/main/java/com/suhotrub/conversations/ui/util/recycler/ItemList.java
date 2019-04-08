package com.suhotrub.conversations.ui.util.recycler;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController;
import com.suhotrub.conversations.ui.util.recycler.controller.DoubleBindableItemController;
import com.suhotrub.conversations.ui.util.recycler.controller.NoDataItemController;
import com.suhotrub.conversations.ui.util.recycler.controller.StickyBindableItemController;
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder;
import com.suhotrub.conversations.ui.util.recycler.holder.DoubleBindableViewHolder;
import com.suhotrub.conversations.ui.util.recycler.item.BaseItem;
import com.suhotrub.conversations.ui.util.recycler.item.BindableItem;
import com.suhotrub.conversations.ui.util.recycler.item.DoubleBindableItem;
import com.suhotrub.conversations.ui.util.recycler.item.NoDataItem;
import com.suhotrub.conversations.ui.util.recycler.item.StickyBindableItem;
import com.suhotrub.conversations.ui.util.recycler.sticky.StickyCallback;

public class ItemList extends ArrayList<BaseItem> {

    public interface BindableItemControllerProvider<T> {
        BindableItemController<T, ? extends BindableViewHolder<T>> provide(T data);
    }

    public interface DoubleBindableItemControllerProvider<T1, T2> {
        DoubleBindableItemController<T1, T2, ? extends DoubleBindableViewHolder<T1, T2>> provide(T1 data);
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

    public ItemList insertIf(boolean condition,
                             int index,
                             BaseItem baseItem) {
        return condition ? insert(index, baseItem) : this;
    }

    public <T> ItemList insert(int index,
                               T data,
                               BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insert(index, new BindableItem<>(data, itemController));
    }

    public <T> ItemList insertIf(boolean condition,
                                 int index,
                                 T data,
                                 BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertIf(condition, index, new BindableItem<>(data, itemController));
    }

    public ItemList insert(int index,
                           NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return this.insert(index, new NoDataItem<>(itemController));
    }

    public ItemList insertIf(boolean condition,
                             int index,
                             NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return insertIf(condition, index, new NoDataItem<>(itemController));
    }

    //single add

    public ItemList addItem(BaseItem item) {
        return insert(this.size(), item);
    }

    public ItemList addIf(boolean condition,
                          BaseItem item) {
        return condition ? addItem(item) : this;
    }

    public <T> ItemList add(T data,
                            BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new BindableItem<>(data, itemController));
    }

    public <T> ItemList addIf(boolean condition,
                              T data,
                              BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addIf(condition, new BindableItem<>(data, itemController));
    }

    public <T1, T2> ItemList add(T1 firstData,
                                 T2 secondData,
                                 DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    public <T1, T2> ItemList addIf(boolean condition,
                                   T1 firstData,
                                   T2 secondData,
                                   DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addIf(condition, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    public ItemList add(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new NoDataItem<>(itemController));
    }

    public ItemList addIf(boolean condition,
                          NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addIf(condition, new NoDataItem<>(itemController));
    }

    //collection insert

    public ItemList insertAll(int index,
                              Collection<BaseItem> items) {
        this.addAll(index, items);
        return this;
    }

    public ItemList addAllItems(Collection<BaseItem> items) {
        return insertAll(this.size(), items);
    }

    public ItemList insertAllIf(boolean condition,
                                int index,
                                Collection<BaseItem> items) {
        return condition ? insertAll(index, items) : this;
    }

    public ItemList addAllIf(boolean condition, Collection<BaseItem> items) {
        return insertAllIf(condition, this.size(), items);
    }

    public <T> ItemList insertAll(int index,
                                  Collection<T> data,
                                  BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAll(index, data, dataItem -> itemController);
    }

    public <T> ItemList addAll(Collection<T> data,
                               BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAll(this.size(), data, itemController);
    }

    public <T> ItemList insertAllIf(boolean condition,
                                    int index,
                                    Collection<T> data,
                                    BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAllIf(condition, index, data, dataItem -> itemController);
    }

    public <T> ItemList addAllIf(boolean condition,
                                 Collection<T> data,
                                 BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAllIf(condition, this.size(), data, itemController);
    }

    public <T> ItemList insertAll(int index,
                                  Collection<T> data,
                                  BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAll(index, create(data, itemControllerProvider));
    }

    public <T> ItemList addAll(Collection<T> data,
                               BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAll(this.size(), data, itemControllerProvider);
    }

    public <T> ItemList insertAllIf(boolean condition,
                                    int index,
                                    Collection<T> data,
                                    BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAllIf(condition, index, create(data, itemControllerProvider));
    }

    public <T> ItemList addAllIf(boolean condition,
                                 Collection<T> data,
                                 BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAllIf(condition, this.size(), data, itemControllerProvider);
    }

    //add headers

    public ItemList addHeader(BaseItem item) {
        return insert(0, item);
    }

    public ItemList addHeaderIf(boolean condition, BaseItem item) {
        return condition ? addHeader(item) : this;
    }

    public <T> ItemList addHeader(T data,
                                  BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeader(new BindableItem<>(data, itemController));
    }

    public <T> ItemList addHeaderIf(boolean condition,
                                    T data,
                                    BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeaderIf(condition, new BindableItem<>(data, itemController));
    }

    public <T1, T2> ItemList addHeader(T1 firstData,
                                       T2 secondData,
                                       DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeader(new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    public <T1, T2> ItemList addHeaderIf(boolean condition,
                                         T1 firstData,
                                         T2 secondData,
                                         DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeaderIf(condition, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    public ItemList addHeader(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addHeader(new NoDataItem<>(itemController));
    }

    public ItemList addHeaderIf(boolean condition,
                                NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addHeaderIf(condition, new NoDataItem<>(itemController));
    }

    public ItemList addStickyHeader(StickyBindableItem item) {
        return addItem(item);
    }

    public ItemList addStickyHeaderIf(boolean condition, StickyBindableItem item) {
        return condition ? addItem(item) : this;
    }

    /**
     * Автоматизированное добавление Sticky Header в текущий лист с кастомизируемой политикой
     * добавления.
     * <p>
     * ВАЖНО: для корректного добавления Sticky Headers в лист требуется вызывать данный метод только
     * после того, как лист уже будет содержать в себе все необходимые данные для отображения.
     * <p>
     * Для работы со Sticky Headers требуется использовать {@link StickyEasyAdapter}, который берёт
     * на себя всю ответственность по позиционированию заголовков.
     *
     * @param stickyCallback реализация текущей политики добавления Sticky Headers;
     * @param itemController sticky-контроллер;
     * @param <T>            тип значения, принимаемого sticky-контроллером
     * @return лист с добавленными в нужных местах Sticky Headers.
     */
    public <T> ItemList addStickyHeaderIf(StickyCallback<T> stickyCallback,
                                          StickyBindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        for (int i = 0; i < this.size(); i++) {
            BindableItem prevItem = null;
            if (i > 0) {
                if (!(this.get(i - 1) instanceof BindableItem)) {
                    continue;
                }
                prevItem = (BindableItem) this.get(i - 1);
            }
            if (!(this.get(i) instanceof BindableItem)) {
                continue;
            }
            BindableItem nextItem = (BindableItem) this.get(i);
            if (nextItem != null) {
                T stickyData = stickyCallback.onSticky(prevItem != null ? prevItem.getData() : null, nextItem.getData());
                if (stickyData != null) {
                    insert(i, new StickyBindableItem<>(stickyData, itemController));
                }
            }
        }
        return this;
    }

    // add footer

    public ItemList addFooter(BaseItem item) {
        return insert(this.size(), item);
    }

    public ItemList addFooterIf(boolean condition, BaseItem item) {
        return condition ? addFooter(item) : this;
    }

    public <T> ItemList addFooter(T data,
                                  BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new BindableItem<>(data, itemController));
    }

    public <T> ItemList addFooterIf(boolean condition,
                                    T data,
                                    BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return condition ? addFooter(data, itemController) : this;
    }

    public <T1, T2> ItemList addFooter(T1 firstData,
                                       T2 secondData,
                                       DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    public <T1, T2> ItemList addFooterIf(boolean condition,
                                         T1 firstData,
                                         T2 secondData,
                                         DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addFooterIf(condition, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    public ItemList addFooter(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new NoDataItem<>(itemController));
    }

    public ItemList addFooterIf(boolean condition,
                                NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addFooterIf(condition, new NoDataItem<>(itemController));
    }
}
