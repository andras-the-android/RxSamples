package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class Creation {


    fun just() {

//   values always remain the same
//
//        onSubscribe
//        1583081328579
//        1583081328579
//        onComplete
//        onSubscribe
//        1583081328579
//        1583081328579
//        onComplete
        val observable = Observable.just(System.currentTimeMillis(), System.currentTimeMillis())
        val observer = object : Observer<Long> {
            override fun onComplete() {
                log("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                log("onSubscribe")
            }

            override fun onNext(t: Long) {
                log(t.toString())
            }

            override fun onError(e: Throwable) {
            }
        }

        observable.subscribe(observer)
        Thread.sleep(500)
        observable.subscribe(observer)

    }

    fun fromCallable() {

//Values are generated at subscription time
//
//D: onSubscribe 1583081988815
//D: 1583081988815
//D: onComplete
//D: onSubscribe 1583081989317
//D: 1583081989317
//D: onComplete

        val observable = Observable.fromCallable { System.currentTimeMillis() }
        val observer = object : Observer<Long> {
            override fun onComplete() {
                log("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                log("onSubscribe ${System.currentTimeMillis()}")
            }

            override fun onNext(t: Long) {
                log(t.toString())
            }

            override fun onError(e: Throwable) {
            }
        }

        Thread.sleep(500)
        observable.subscribe(observer)
        Thread.sleep(500)
        observable.subscribe(observer)

    }

    fun create() {
//        D: 1
//        D: 2
//        D: 3
//        D: completed

        //alternatively:
        //Observable.create {  emitter: ObservableEmitter<Int> ->
        Observable.create<Int> {  emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
            emitter.onComplete()
        }.subscribe({ log(it.toString()) }, { }, { log("completed") })
    }

    fun defer() {

//        Values are generated at subscription time
//
//        D: onSubscribe
//        D: 1583083510448
//        D: 1583083510448
//        D: onComplete
//        D: onSubscribe
//        D: 1583083510950
//        D: 1583083510950
//        D: onComplete
        val observable = Observable.defer { Observable.just(System.currentTimeMillis(), System.currentTimeMillis()) }
        val observer = object : Observer<Long> {
            override fun onComplete() {
                log("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                log("onSubscribe")
            }

            override fun onNext(t: Long) {
                log(t.toString())
            }

            override fun onError(e: Throwable) {
            }
        }

        observable.subscribe(observer)
        Thread.sleep(500)
        observable.subscribe(observer)
    }

    fun empty() {
//        completes instantly
//        D: onCompleted

        Observable.empty<String>()
            .subscribe( { log("onNext") }, { log("onError") }, { log("onCompleted") } )
    }

    fun never() {
//        it never will do anything
        Observable.never<String>()
            .subscribe( { log("onNext") }, { log("onError") }, { log("onCompleted") } )
    }

    fun error() {
//        triggers an error
//        D: exception message
        Observable.error<Exception>(IOException("exception message"))
            .subscribe( { log("onNext") }, { log(it.message!!) }, { log("onCompleted") } )
    }

    fun range() {
//        D: 3
//        D: 4
//        D: 5
//        D: 6
//        D: 7
        Observable.range(3, 5)
            .subscribe( { log(it.toString()) }, { log("onError") }, { log("onCompleted") } )
    }

    fun interval() {
//        It emits an increasing series of Long in every 500 milliseconds and it never completes
//
//        D: 0
//        D: 1
//        D: 2
//        D: 3
//        D: 4
//        .....
        Observable.interval(500L, TimeUnit.MILLISECONDS)
            .subscribe( { log(it.toString()) }, { log("onError") }, { log("onCompleted") } )
    }

    fun intervalRange() {
//        Combination of interval and range. After the subscription it waits 2 seconds and then emits the series with 500ms deley bitween the items
//
//        D: onSubscribe
//        D: 100
//        D: 101
//        D: 102
//        D: 103
//        D: 104
//        D: onCompleted
        Observable.intervalRange(100L, 5L, 2000L, 500L, TimeUnit.MILLISECONDS)
            .doOnSubscribe { log("onSubscribe") }
            .subscribe( { log(it.toString()) }, { log("onError") }, { log("onCompleted") } )
    }

    fun timer() {
//        Emits a 0L value with the defined delay
//
//        D: onSubscribe
//        D: 0
//        D: onCompleted
        Observable.timer(3000L, TimeUnit.MILLISECONDS)
            .doOnSubscribe { log("onSubscribe") }
            .subscribe( { log(it.toString()) }, { log("onError") }, { log("onCompleted") } )
    }

    fun latest() {

    }


}