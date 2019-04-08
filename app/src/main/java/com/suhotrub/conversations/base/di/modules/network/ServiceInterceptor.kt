package com.suhotrub.conversations.base.di.modules.network;

import com.suhotrub.conversations.interactor.user.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Интерцептор, включающий в каждый запрос необходимые параметры:
 * * токен авторизации;
 * * локаль устройства.
 */
@Singleton
class ServiceInterceptor @Inject constructor(
        private val tokenStorage: TokenStorage
) : Interceptor {

    private val HEADER_AUTH_KEY = "Authorization"
    private val HEADER_DEVICE_KEY = "FM-Device-Id"
    private val HEADER_DEVICE_LOCATION_LAT = "FM-Device-Location-Lat"
    private val HEADER_DEVICE_LOCATION_LON = "FM-Device-Location-Lon"
    private val HEADER_DEVICE_LOCATION_CITY = "FM-Device-Location-City"

    private val HEADER_FCM_KEY = "FM-FCM-Token"
    private val HEADER_PLATFORM_KEY = "FM-Device-Platform"
    private val HEADER_VERSION_KEY = "FM-App-Version"
    private val HEADER_LANG = "FM-Lang"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()


        if (originalRequest.headers().get(HEADER_AUTH_KEY) != null) {
            return chain.proceed(originalRequest)
        }
        /*

        val headersBuilder = originalRequest.headers().newBuilder()

        headersBuilder.add(HEADER_DEVICE_KEY, deviceIdStorage.getDeviceId())
                .add(HEADER_FCM_KEY, fcmTokenProvider.provide() ?: EMPTY_STRING)
                .add(HEADER_PLATFORM_KEY, BuildConfig.PLATFORM)
                .add(HEADER_VERSION_KEY, BuildConfig.VERSION_NAME)
                .add(HEADER_LANG, getLanguage())*/





        return chain.proceed(
                originalRequest
                        .newBuilder()
                        .headers(
                                originalRequest
                                        .headers()
                                        .newBuilder()
                                        .add(HEADER_AUTH_KEY, "Bearer ${tokenStorage.getToken()}")
                                        .build()
                        )
                        .build()
        )
    }
}