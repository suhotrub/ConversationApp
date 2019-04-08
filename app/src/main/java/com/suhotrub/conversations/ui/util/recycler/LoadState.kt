package com.suhotrub.conversations.ui.util.recycler

enum class LoadState {
    UNSPECIFIED, //не определен
    NONE, //контент загружен
    MAIN_LOADING, //прогресс, закрывающий весь контент
    TRANSPARENT_LOADING, //полупрозрачный прогресс, блокирует весь интерфейс
    ERROR, //ошибка загрузки данных
    EMPTY, //данных нет
    NOT_FOUND
    //нет данных по заданному фильтру
}
