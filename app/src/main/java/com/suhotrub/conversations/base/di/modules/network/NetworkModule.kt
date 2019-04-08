package com.suhotrub.conversations.base.di.modules.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.suhotrub.conversations.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/**
 * Dagger-модуль для удовлетворения зависимостей инструментов клиент-серверного взаимодействия
 */
@Module
class NetworkModule {

    private val HTTP_LOG_TAG = "OkHttp"

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 callAdapterFactory: CallAdapterFactory,
                                 gson: Gson,
                                 apiUrl: String) =
            Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(callAdapterFactory)
                    .build()

    @Provides
    @Singleton
    internal fun provideGson() = GsonBuilder().create()

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message -> Timber.tag(HTTP_LOG_TAG).d(message) }
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        return logging
    }
}