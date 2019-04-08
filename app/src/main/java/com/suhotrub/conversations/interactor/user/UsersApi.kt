package com.suhotrub.conversations.interactor.user

import com.suhotrub.conversations.model.user.UserLoginDto
import com.suhotrub.conversations.model.user.UserSignupDto
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersApi {


    @POST("/Users/signup")
    fun signUp(
            @Body body: UserSignupDto
    ): Observable<String>

    @POST("/Users/login")
    fun login(
            @Body body: UserLoginDto
    ): Observable<String>

    @GET("/Users/get_username")
    fun getUsername(): Observable<String>

    @GET("/Users/find")
    fun getUsersByLogin(
            @Query("login") login: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<List<UserSignupDto>>

}