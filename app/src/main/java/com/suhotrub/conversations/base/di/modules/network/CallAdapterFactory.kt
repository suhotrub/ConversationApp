package com.suhotrub.conversations.base.di.modules.network

import io.reactivex.Observable
import io.reactivex.ObservableSource
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallAdapterFactory @Inject constructor() : CallAdapter.Factory() {


    private val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
    private val retryCalls: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    private val singleThreadExecutor: Executor = Executors.newSingleThreadExecutor()

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        val rxCallAdapter = rxJava2CallAdapterFactory.get(returnType, annotations, retrofit) as? CallAdapter<*, Observable<*>>
        return ResultCallAdapter(returnType, rxCallAdapter)
    }

    inner class ResultCallAdapter<T>(returnType: Type?,
                                     val rxCallAdapter: CallAdapter<T, Observable<*>>?) : CallAdapter<T, Observable<*>> {

        private val responseType: Type = getParameterUpperBound(0, returnType as ParameterizedType)

        override fun responseType(): Type = responseType

        override fun adapt(call: Call<T>?): Observable<*>? = rxCallAdapter?.adapt(call)
                ?.onErrorResumeNext { e: Throwable -> handleNetworkError(e, call) as ObservableSource<out Nothing> }

        private fun <R> handleNetworkError(e: Throwable, call: Call<R>?): Observable<R> =
                Observable.error<R>(e)

    }
}