package com.suhotrub.conversations.ui.util.recycler;


import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.suhotrub.conversations.ui.util.recycler.controller.BaseItemController;
import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController;
import com.suhotrub.conversations.ui.util.recycler.controller.NoDataItemController;
import com.suhotrub.conversations.ui.util.recycler.holder.BaseViewHolder;
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder;
import com.suhotrub.conversations.ui.util.recycler.item.BaseItem;
import com.suhotrub.conversations.ui.util.recycler.item.NoDataItem;

public class EasyAdapter extends RecyclerView.Adapter {

    public static final int INFINITE_SCROLL_LOOPS_COUNT = 100;

    private List<BaseItem> items = new ArrayList<>();
    private List<ItemInfo> lastItemsInfo = new ArrayList<>();
    protected SparseArray<BaseItemController> supportedItemControllers = new SparseArray<>();
    private boolean autoNotifyOnSetItemsEnabled = true;
    private boolean firstInvisibleItemEnabled = false;
    private BaseItem<BaseViewHolder> firstInvisibleItem = new NoDataItem<>(new FirstInvisibleItemController());

    private boolean infiniteScroll;

    public EasyAdapter() {
        setHasStableIds(true);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (firstInvisibleItemEnabled) initLayoutManager(recyclerView.getLayoutManager());
    }

    @Override
    public final int getItemViewType(int position) {
        return items.get(getListPosition(position)).getItemController().viewType();
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return supportedItemControllers.get(viewType).createViewHolder(parent);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseItem item = items.get(getListPosition(position));
        item.getItemController().bind(holder, item);
    }

    @Override
    public final int getItemCount() {
        return infiniteScroll ? INFINITE_SCROLL_LOOPS_COUNT * items.size() : items.size();
    }

    @Override
    public final long getItemId(int position) {
        BaseItem item = items.get(getListPosition(position));
        return item.getItemController().getItemId(item);
    }

    /**
     * Toggle whether {@link FirstInvisibleItemController} is enabled
     *
     * @see FirstInvisibleItemController
     */
    public void setFirstInvisibleItemEnabled(boolean enableFirstInvisibleItem) {
        this.firstInvisibleItemEnabled = enableFirstInvisibleItem;
    }

    /**
     * automatically calls necessary methods notify...
     */
    public void autoNotify() {
        final List<ItemInfo> newItemInfo = extractRealItemInfo();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new AutoNotifyDiffCallback(lastItemsInfo, newItemInfo));
        diffResult.dispatchUpdatesTo(this);
        lastItemsInfo = newItemInfo;
    }


    public final long getItemHash(int position) {
        BaseItem item = items.get(getListPosition(position));
        return item.getItemController().getItemHash(item);
    }

    /**
     * set Items for rendering
     *
     * @param items
     * @param autoNotify need call {@link #autoNotify()}
     */
    protected void setItems(@NonNull ItemList items, boolean autoNotify) {
        this.items.clear();
        if (firstInvisibleItemEnabled && (items.isEmpty() || items.get(0) != firstInvisibleItem)) {
            this.items.add(firstInvisibleItem);
        }
        this.items.addAll(items);

        if (autoNotify) {
            autoNotify();
        }
        updateSupportedItemControllers(this.items);
    }

    protected ItemList getItems() {
        return new ItemList(items);
    }

    /**
     * set Items for rendering
     * adapter automatically calls necessary methods notify... if {@link #autoNotifyOnSetItemsEnabled} sets
     *
     * @param items
     */
    public void setItems(@NonNull ItemList items) {
        setItems(items, autoNotifyOnSetItemsEnabled);
    }

    private void updateSupportedItemControllers(List<BaseItem> items) {
        supportedItemControllers.clear();
        for (BaseItem item : items) {
            BaseItemController itemController = item.getItemController();
            supportedItemControllers.put(itemController.viewType(), itemController);
        }
    }

    /**
     * extract real items info, despite of infinite or ordinary scroll
     */
    private List<ItemInfo> extractRealItemInfo() {
        int itemCount = items.size();
        List<ItemInfo> currentItemsInfo = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            currentItemsInfo.add(new ItemInfo(
                    getItemId(i),
                    getItemHash(i)));
        }
        return currentItemsInfo;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof BindableViewHolder) {
            ((BindableViewHolder) holder).recycle();
        }
    }

    private void initLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager castedLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup existingLookup = castedLayoutManager.getSpanSizeLookup();

            castedLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        //full first invisible element
                        return castedLayoutManager.getSpanCount();
                    } else {
                        return existingLookup.getSpanSize(position);
                    }
                }
            });
        }
    }

    private int getListPosition(int adapterPosition) {
        return infiniteScroll
                ? adapterPosition % items.size()
                : adapterPosition;
    }

    private class AutoNotifyDiffCallback extends DiffUtil.Callback {
        private static final long MAGIC_NUMBER = 3578121127L; // used for making ids unique

        private final List<ItemInfo> lastItemsInfo;
        private final List<ItemInfo> newItemsInfo;

        AutoNotifyDiffCallback(List<ItemInfo> lastItemsInfo,
                               List<ItemInfo> newItemsInfo) {
            this.lastItemsInfo = lastItemsInfo;
            this.newItemsInfo = newItemsInfo;
        }

        @Override
        public int getOldListSize() {
            if (infiniteScroll) {
                return lastItemsInfo.size() * INFINITE_SCROLL_LOOPS_COUNT;
            } else {
                return lastItemsInfo.size();
            }
        }

        @Override
        public int getNewListSize() {
            if (infiniteScroll) {
                return newItemsInfo.size() * INFINITE_SCROLL_LOOPS_COUNT;
            } else {
                return newItemsInfo.size();
            }
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if (infiniteScroll) {
                //magic numbers make every element id unique
                long lastItemsFakeItemId = lastItemsInfo.get(oldItemPosition % lastItemsInfo.size()).getId() + oldItemPosition + MAGIC_NUMBER;
                long newItemsFakeItemId = newItemsInfo.get(newItemPosition % newItemsInfo.size()).getId() + newItemPosition + MAGIC_NUMBER;

                return lastItemsFakeItemId == newItemsFakeItemId;
            }
            return lastItemsInfo.get(oldItemPosition).getId() ==
                    newItemsInfo.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            if (infiniteScroll) {
                oldItemPosition = oldItemPosition % lastItemsInfo.size();
                newItemPosition = newItemPosition % newItemsInfo.size();
            }
            return lastItemsInfo.get(oldItemPosition).getHash() ==
                    newItemsInfo.get(newItemPosition).getHash();
        }
    }

    private class ItemInfo {
        private long id;
        private long hash;

        ItemInfo(long id, long hash) {
            this.id = id;
            this.hash = hash;
        }

        long getId() {
            return id;
        }

        long getHash() {
            return hash;
        }
    }

    /**
     * Empty first element for saving scroll position after notify... calls
     */
    private class FirstInvisibleItemController extends NoDataItemController<BaseViewHolder> {
        @Override
        public BaseViewHolder createViewHolder(ViewGroup parent) {
            ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(1, 1); // установить размер 1px, иначе проблемы с swipe-to-refresh и drag&drop https://github.com/airbnb/epoxy/issues/74
            View itemView = new View(parent.getContext());
            itemView.setLayoutParams(lp);
            return new BaseViewHolder(itemView);
        }
    }
}