package com.suhotrub.conversations.interactor.groups

import com.suhotrub.conversations.model.group.GroupCreateDto
import com.suhotrub.conversations.model.group.GroupInviteDto
import com.suhotrub.conversations.model.group.GroupsDto
import com.suhotrub.conversations.model.user.UsersDto
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*
import javax.inject.Singleton

/**
 * HTTP запросы для работы с группой
 */
@Singleton
interface GroupsApi {

    /**
     * Приглашение пользователя в группу
     * @param body параметры приглашения
     */
    @POST("Groups/invite")
    fun invite(
            @Body groupInviteDto: GroupInviteDto
    ): Observable<ResponseBody>

    @POST("Groups/leave")
    fun leave(
            @Query("groupGuid") groupGuid: String
    ): Observable<ResponseBody>

    /**
     * Создает группу
     * @param body параметры группы
     */
    @POST("Groups/create")
    fun create(
            @Body body: GroupCreateDto
    ): Observable<ResponseBody>

    /**
     * Возвращает группы, в которых состоит текущий пользователь
     * @param pageSize размер страницы
     * @param page номер страницы
     */
    @GET("Groups/joined")
    fun getJoinedGroups(
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<GroupsDto>

    /**
     * Возвращает пользователей состоящих в группе
     * @param groupGuid guid группы
     * @param pageSize размер страницы
     * @param page номер страницы
     */
    @GET("Groups/participants")
    fun getGroupParticicpants(
            @Query("groupGuid") groupGuid: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<UsersDto>

}