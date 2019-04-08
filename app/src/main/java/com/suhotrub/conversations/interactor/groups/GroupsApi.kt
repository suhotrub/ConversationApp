package com.suhotrub.conversations.interactor.groups

import com.suhotrub.conversations.model.group.GroupCreateDto
import com.suhotrub.conversations.model.group.GroupDto
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupsApi {


    @POST("/Groups/join")
    fun join(
            @Query("groupsName") groupName: String
    ): Observable<ResponseBody>

    @POST("/Groups/create")
    fun create(
            @Body body: GroupCreateDto
    ): Observable<ResponseBody>

    @GET("/Groups/joined")
    fun getJoinedGroups(
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<List<GroupDto>>

    @GET("/Groups/created")
    fun getCreatedGroups(
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<List<GroupDto>>

    @GET("/Groups/participants")
    fun getGroupParticicpants(
            @Query("groupsName") groupName: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<String>

    @GET("/Groups/find")
    fun getGroupsByName(
            @Query("name") name: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<List<GroupDto>>

}