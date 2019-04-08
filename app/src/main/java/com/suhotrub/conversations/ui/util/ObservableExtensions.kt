package com.suhotrub.conversations.ui.util

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun <T> Observable<T>.then(listener: (T) -> Unit): Observable<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(listener)
fun isDisposableActive(disposable: Disposable?) = disposable != null && !disposable.isDisposed
fun <T> subscribeIoHandleError(observable: Observable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) = observable?.then(onNext)?.doOnError(onError)?.subscribe()
fun <T> subscribe(observable: Observable<T>?, onNext: (T) -> Unit) = subscribeIoHandleError(observable, onNext) {
    Timber.e(it)
}

fun <T> Flowable<T>.then(listener: (T) -> Unit): Flowable<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(listener)
fun <T> subscribeIoHandleError(observable: Flowable<T>?, onNext: (T) -> Unit, onError: (Throwable) -> Unit) = observable?.then(onNext)?.doOnError(onError)?.subscribe()
fun <T> subscribe(observable: Flowable<T>?, onNext: (T) -> Unit) = subscribeIoHandleError(observable, onNext) {
    Timber.e(it)
}