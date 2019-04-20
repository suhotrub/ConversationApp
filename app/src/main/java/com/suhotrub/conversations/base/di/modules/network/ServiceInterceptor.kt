package com.suhotrub.conversations.base.di.modules.network;

import com.google.firebase.iid.FirebaseInstanceId
import com.suhotrub.conversations.interactor.user.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Интерцептор, включающий в каждый запрос необходимые параметры:
 * * токен авторизации;
 * * локаль устройства.
 * @param tokenStorage хранилище токена авторизации
 */
@Singleton
class ServiceInterceptor @Inject constructor(
        private val tokenStorage: TokenStorage
) : Interceptor {

    /**
     * Заголовок запроса для предоставления токена авторизации
     */
    private val HEADER_AUTH_KEY = "Authorization"

    /**
     * Заголовок запроса для предоставления токена для FCM
     */
    private val HEADER_FCM_KEY = "FcmToken"


    /**
     * Встраивает заголовки в запрос
     * @param chain исходный запрос
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        if (originalRequest.headers().get(HEADER_AUTH_KEY) != null) {
            return chain.proceed(originalRequest)
        }

        return chain.proceed(
                originalRequest
                        .newBuilder()
                        .headers(
                                originalRequest
                                        .headers()
                                        .newBuilder()
                                        .add(HEADER_AUTH_KEY, "Bearer ${tokenStorage.getToken()}")
                                        .add(HEADER_FCM_KEY, FireBaseToken.token.orEmpty())
                                        .build()
                        )
                        .build()
        )
    }
}

