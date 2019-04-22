package com.suhotrub.conversations.ui.util.recycler.item;

import android.support.v7.widget.RecyclerView;

import com.suhotrub.conversations.ui.util.recycler.controller.BaseItemController;

    public class BaseItem<H extends RecyclerView.ViewHolder> {

    private BaseItemController<H, ? extends BaseItem> itemController;

    public BaseItem(BaseItemController<H, ? extends BaseItem> itemController) {
        this.itemController = itemController;
    }

    public BaseItemController<H, ? extends BaseItem> getItemController() {
        return itemController;
    }
}