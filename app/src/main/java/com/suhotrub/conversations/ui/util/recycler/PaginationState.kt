package com.suhotrub.conversations.ui.util.recycler

enum class PaginationState {
    READY, //готово к пагинации или запрос на пагинацию выполняется
    COMPLETE, //все данные загружены
    ERROR, //при загрузке данных произошла ошибка
    LOADING //показывается лоадинг статус, это состояние не нужно устанавливать из презентера
}
