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
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger-модуль для удовлетворения зависимостей инструментов клиент-серверного взаимодействия
 */
@Module
class NetworkModule {

    /**
     * Тэг для логгирования
     */
    private val HTTP_LOG_TAG = "OkHttp"

    /**
     * Поставляет базовый URL
     */
    @Provides
    @Named("BASE_URL")
    @Singleton
    fun provideBaseUrl() = "https://webrtc.ninja/api/"

    /**
     * Возвращает Retrifot-клиент для работы с запросами
     * @param okHttpClient клиент для работы с HTTP запросами
     * @param callAdapterFactory фабрика для адаптеров результата выполнения запросов
     * @param gson парсер JSON в объекты Java
     * @param apiUrl базовый URL API
     */
    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 callAdapterFactory: CallAdapterFactory,
                                 gson: Gson,
                                 @Named("BASE_URL") apiUrl: String) =
            Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(callAdapterFactory)
                    .build()

    /**
         * Возвращает парсер JSON в объекты Java
     */
    @Provides
    @Singleton
    internal fun provideGson() = GsonBuilder().create()


    /**
     * Возвращает interceptor для логгирования запросов
     */
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