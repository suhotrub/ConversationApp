package com.suhotrub.conversations.interactor.groups

import com.suhotrub.conversations.model.group.GroupCreateDto
import com.suhotrub.conversations.model.group.GroupInviteDto
import com.suhotrub.conversations.model.group.GroupsDto
import com.suhotrub.conversations.model.user.UsersDto
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface GroupsApi {


    @POST("Groups/invite")
    fun invite(
            @Body groupName: GroupInviteDto
    ): Observable<ResponseBody>

    @POST("Groups/create")
    fun create(
            @Body body: GroupCreateDto
    ): Observable<ResponseBody>

    @GET("Groups/joined")
    fun getJoinedGroups(
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<GroupsDto>

    @GET("Groups/created")
    fun getCreatedGroups(
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<GroupsDto>

    @GET("Groups/participants")
    fun getGroupParticicpants(
            @Query("groupGuid") groupName: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<UsersDto>

    @GET("Groups/find")
    fun getGroupsByName(
            @Query("pattern") name: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<GroupsDto>

}