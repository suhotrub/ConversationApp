package com.suhotrub.conversations.interactor.user

import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.model.user.UserLoginDto
import com.suhotrub.conversations.model.user.UserSignupDto
import com.suhotrub.conversations.model.user.UsersDto
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
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
            @Query("pattern") login: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<UsersDto>

    @GET("/Users/current")
    fun getCurrentUser(): Observable<UserDto>

}