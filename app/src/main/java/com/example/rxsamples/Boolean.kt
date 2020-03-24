package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable

@SuppressLint("CheckResult")
class Boolean {

//    Returns a Single that emits a true value if the condition is true for all item in the source observable
//

    fun all() {
        //    D: true
        Observable.just(1, 2, 3).all { it < 10 }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })

        //    D: false
        Observable.just(1, 2, 3, 11).all { it < 10 }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

    fun contains() {
//        D: false
        Observable.just(1, 2, 3, 11).contains(10)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })

//        D: true
        Observable.just(1, 2, 3, 11).contains(11)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

    fun isEmpty() {
        //false
        Observable.just(1, 2, 3, 11).isEmpty
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })

        //true
        Observable.empty<Int>().isEmpty
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

    fun sequenceEqual() {
        //false
        val o1 = Observable.just(1, 2, 3, 4)
        val o2 = Observable.just(1, 2, 3, 4, 5)
        Observable.sequenceEqual(o1, o2)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }


    fun latest() {
    }
}