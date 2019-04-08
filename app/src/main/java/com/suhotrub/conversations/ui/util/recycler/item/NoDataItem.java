package com.suhotrub.conversations.ui.util.recycler.item;


import android.support.v7.widget.RecyclerView;

import com.suhotrub.conversations.ui.util.recycler.controller.NoDataItemController;

public final class NoDataItem<H extends RecyclerView.ViewHolder>
        extends BaseItem<H> {

    public NoDataItem(NoDataItemController<H> itemController) {
        super(itemController);
    }

}
