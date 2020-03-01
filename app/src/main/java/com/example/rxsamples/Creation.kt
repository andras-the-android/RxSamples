package com.example.rxsamples

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.Callable

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

    fun latest() {

    }


}