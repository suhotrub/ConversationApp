package com.suhotrub.conversations.ui.activities.group

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState

interface GroupActivityView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderMessages(messages: List<MessageDto>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderGroup(groupDto: GroupDto)

    @StateStrategyType(SkipStrategy::class)
    fun showErrorSnackbar(t: Throwable)

    @StateStrategyType(SkipStrategy::class)
    fun setPaginationState(paginationState: PaginationState)

    @StateStrategyType(SkipStrategy::class)
    fun clearMessage()

    @StateStrategyType(SkipStrategy::class)
    fun scrollToBottom()
}