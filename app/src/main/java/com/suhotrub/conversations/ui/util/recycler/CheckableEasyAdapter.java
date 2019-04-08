package com.suhotrub.conversations.ui.util.recycler;

import com.suhotrub.conversations.ui.util.recycler.controller.CheckableItemController;

/**
 * EasyAdapter с поддержкой одиночного выделения элемента.
 */
public class CheckableEasyAdapter extends EasyAdapter {

    @Override
    public void setItems(ItemList itemss) {
        super.setItems(itemss);

        if (supportedItemControllers.size() > 0) {
            long firstItemId = supportedItemControllers.valueAt(0).getItemId(getItems().get(0));

            for (int i = 0; i < supportedItemControllers.size(); i++) {
                if (supportedItemControllers.valueAt(i) instanceof CheckableItemController) {
                    CheckableItemController checkableItemController = (CheckableItemController) supportedItemControllers.valueAt(i);
                    if (i == 0) {
                        checkableItemController.setCheckedItemId(firstItemId);
                        autoNotify();
                    }
                    checkableItemController.setOnCheckItemListener((checkedItemId, controller) -> {
                        controller.setCheckedItemId(checkedItemId);
                        autoNotify();
                    });
                }
            }
        }
    }
}