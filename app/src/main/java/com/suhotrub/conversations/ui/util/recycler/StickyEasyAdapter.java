package com.suhotrub.conversations.ui.util.recycler;

import android.support.v7.widget.RecyclerView;

import com.suhotrub.conversations.ui.util.recycler.sticky.StickyLayoutManager;

/**
 * EasyAdapter, упрощающий работу со Sticky Headers
 */
public class StickyEasyAdapter extends EasyAdapter {

    public StickyEasyAdapter(RecyclerView recyclerView) {
        StickyLayoutManager stickyLayoutManager = new StickyLayoutManager(recyclerView.getContext(), this::getItems);
        recyclerView.setLayoutManager(stickyLayoutManager);
        recyclerView.setAdapter(this);
    }

}