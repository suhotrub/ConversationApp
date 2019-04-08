package com.suhotrub.conversations.ui.util.recycler.sticky;

import java.util.List;

public interface StickyHeaderHandler {

    /**
     * @return The dataset supplied to the {@link RecyclerView.Adapter}
     */
    List<?> getAdapterData();
}