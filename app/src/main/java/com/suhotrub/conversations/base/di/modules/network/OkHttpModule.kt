package com.suhotrub.conversations.base.di.modules.network

import android.os.Build
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.security.KeyStore
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


/**
 * Dagger-модуль для удовлетворения зависимости OkHttp-клиента. Вынесен отдельно для возможности замены его при интеграционном тестировании.
 */
@Module
class OkHttpModule {

    /**
     * Таймаут запросов в секундах
     */
    private val timeout: Long = 40


    /**
     * Возвращает OkHTTP клиент для работы с запросами
     */
    @Provides
    @Singleton
    internal fun provideOkHttpClient(serviceInterceptor: ServiceInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            connectTimeout(timeout, TimeUnit.SECONDS)
            readTimeout(timeout, TimeUnit.SECONDS)
            writeTimeout(timeout, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(serviceInterceptor)
            retryOnConnectionFailure(true)
        }


        return okHttpClientBuilder.build()
    }

}