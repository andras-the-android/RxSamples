package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class RandomThings {


    fun doOnTerminateVsDoOnDisposeVsDoFinally() {

        val observable = Observable.intervalRange(1L, 5L, 0L, 1L, TimeUnit.SECONDS)
            .doOnTerminate { log("terminated") }
            .doOnDispose { log("disposed") }
            .doFinally { log("finally") }

//        D: 1
//        D: 2
//        D: 3
//        D: 4
//        D: 5
//        D: terminated
//        D: completed
//        D: finally
        observable
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

//        D: 1
//        D: 2
//        D: 3
//        D: disposed
//        D: finally
        val disposable = observable
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
        TimeUnit.SECONDS.sleep(2)
        disposable.dispose()
    }

    fun triggeringAnRxFlowByACompletable() {
        Completable.complete().andThen(Observable.range(5, 5)).subscribe { println("onnext $it") }
    }

    fun latest() {


    }
}