package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import java.io.IOException

@SuppressLint("CheckResult")
class Error {

//    D: doOnError: Something went wrong
//    D: error in subscribe: Something went wrong
    fun doOnError() {
    Observable.error<IOException>(IOException("Something went wrong"))
        .doOnError { error -> log("doOnError: ${error.message}") }
        .subscribe( { log(it.toString()) }, { log("error in subscribe: ${it.message}") }, { log("completed") })
    }

//    D: 1
//    D: 2
//    D: 666
//    D: completed
    fun onErrorResumeNext() {
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .onErrorResumeNext ( Observable.just(666) )
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .onErrorResumeNext { _: Throwable -> Observable.just(666) }
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    D: 1
//    D: 2
//    D: 666
//    D: completed
    fun onErrorReturn() {
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .onErrorReturn { _: Throwable -> 666 }
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .onErrorReturnItem(666)
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
        }

//    D: 1
//    D: 2
//    D: 1
//    D: 2
//    D: 1
//    D: 2
//    D: Something went wrong
    fun retry() {
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .retry { retryCount, throwable ->  retryCount < 3}
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

        var retryCount = 0
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            ++retryCount
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .retryUntil {  retryCount >= 3}
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Retry when accept a function with a observable parameter that contains the possible errors and expect another observeble that
//    handles the retry process. If the result observable emits an onNext that triggers a reschedule while the onError is sent to the downstream.
//    With this operator we can create really sophistated retry mechanisms. Good example a polling retry what holds a cretain delay before the retry.
//    D: 1
//    D: 2
//    D: 1
//    D: 2
//    D: 1
//    D: 2
//    D: Something went wrong again
    fun retryWhen() {
        var retryCount = 1
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(IOException("Something went wrong"))
            it.onNext(3)
            it.onNext(4)
        }
            .retryWhen { errors -> errors.flatMap {
                return@flatMap if (retryCount < 3) {
                    ++retryCount
                    //retry
                    Observable.just("random item")
                } else {
                    //send error to downstream
                    Observable.error(IOException("Something went wrong again"))
                }
            } }
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

    fun latest() {

    }

}