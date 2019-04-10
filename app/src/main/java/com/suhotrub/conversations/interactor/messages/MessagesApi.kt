package com.suhotrub.conversations.interactor.messages

import com.suhotrub.conversations.model.messages.MessagesDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MessagesApi {

    @GET("/messages/get_by_group")
    fun getGroupMessages(
            @Query("groupGuid") groupGuid: String,
            @Query("page") page: Int,
            @Query("pageNumber") offset: Int
    ): Observable<MessagesDto>

}