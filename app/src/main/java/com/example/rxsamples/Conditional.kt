package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class Conditional {

//    skipUntil, skipWhile, takeUntil, takeWhile operators are in Filtering.kt



//    amb passes the entire emission of the second (because it emits first) and stops all the other streams
//
//    D: onNext second
//    D: second 0
//    D: onNext second
//    D: second 1
//    D: onNext second
//    D: second 2
//    D: onNext second
//    D: second 3
//    D: onNext second
//    D: second 4
//    D: onNext second
//    D: second 5
//    D: onNext second
//    D: second 6
//    D: onNext second
//    D: second 7
//    D: onNext second
//    D: second 8
//    D: onNext second
//    D: second 9
    fun amb() {
        val first = Observable.interval(300, TimeUnit.MILLISECONDS).map { "first $it" }.doOnNext { log("onNext second") }
        val second = Observable.interval(100, TimeUnit.MILLISECONDS).map { "second $it" }.doOnNext { log("onNext second") }
        val third = Observable.interval(500, TimeUnit.MILLISECONDS).map { "third $it" }.doOnNext { log("onNext third") }
        val disposable = Observable.amb(arrayListOf(first, second, third))
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
        Thread.sleep(1000)
        disposable.dispose()
    }

    fun defaultIfEmpty() {
//        D: 1
//        D: 2
//        D: 3
//        D: completed
        Observable.just(1, 2, 3).defaultIfEmpty(0)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

//        D: 0
//        D: completed
        Observable.empty<Int>().defaultIfEmpty(0)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

//        D: original
        Observable.error<Exception>(Exception("original")).defaultIfEmpty(IOException("default"))
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

    }

    fun latest() {
    }
}