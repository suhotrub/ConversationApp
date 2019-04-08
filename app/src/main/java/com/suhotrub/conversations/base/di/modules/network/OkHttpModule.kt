package com.suhotrub.conversations.base.di.modules.network

import android.content.Context
import android.os.Build
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


/**
 * Dagger-модуль для удовлетворения зависимости OkHttp-клиента.
 * Вынесен отдельно для возможности замены его при интеграционном тестировании.
 */
@Module
class OkHttpModule {

    private val timeout: Long = 40 //секунд

    /**
     * На старых устройствах не поддерживаются современные способы шифрования, по-умлочанию они выключены и их необходимо включить
     */
    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT < 22) {
            try {

                val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
                }
                val trustManager = trustManagers[0] as X509TrustManager

                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory), trustManager)

                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                val specs = mutableListOf<ConnectionSpec>()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)

                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Timber.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }

        }
        return client
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(serviceInterceptor: ServiceInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(timeout, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(timeout, TimeUnit.SECONDS)
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        okHttpClientBuilder.addInterceptor(serviceInterceptor)
        okHttpClientBuilder.retryOnConnectionFailure(true)

        return enableTls12OnPreLollipop(okHttpClientBuilder).build()
    }
}