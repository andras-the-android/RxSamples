package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class Combining {


//    Any of the sources emit a value combineLatest will mirror it (along with the latest value of the other sources)
//    It will not emit anyting until all the sources emitted at least one item
//
//    D: fast: 1, slow: 0
//    D: fast: 2, slow: 0
//    D: fast: 3, slow: 0
//    D: fast: 4, slow: 0
//    D: fast: 4, slow: 1
//    D: fast: 5, slow: 1
//    D: fast: 6, slow: 1
//    D: fast: 7, slow: 1
//    D: fast: 7, slow: 2
//    D: fast: 8, slow: 2
//    D: fast: 9, slow: 2
//    D: fast: 10, slow: 2
//    D: fast: 10, slow: 3
//    D: fast: 11, slow: 3
//    D: fast: 12, slow: 3
    fun combineLatest() {
        val emitFast = Observable.interval(0, 300, TimeUnit.MILLISECONDS)
        val emitSlow = Observable.interval(400, 900, TimeUnit.MILLISECONDS)
        //no matter what the lint say it won't compile without the implicit generics and function declaration, rxkotlin solves the issue
        Observable.combineLatest<Long, Long, String>(emitFast, emitSlow, BiFunction { fastEmission, slowEmission ->
            "fast: $fastEmission, slow: $slowEmission"
        })
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

//        this way we can combine more than two sources but in exchange it's not type safe
        Observable.combineLatest(arrayOf(emitFast, emitSlow)) {
            "fast: ${it[0] as Long}, slow: ${it[1] as Long}"
        }
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
        }

//    D: fast 0
//    D: fast 1
//    D: slow 0
//    D: fast 2
//    D: fast 3
//    D: fast 4
//    D: slow 1
//    D: fast 5
//    D: fast 6
//    D: fast 7
//    D: slow 2
//    D: fast 8
//    D: fast 9
//    D: fast 10
//    D: slow 3
//    D: completed
    fun merge() {
        val emitFast = Observable.interval(0, 300, TimeUnit.MILLISECONDS).map { "fast $it" }
        val emitSlow = Observable.interval(400, 900, TimeUnit.MILLISECONDS).map { "slow $it" }
        emitFast.mergeWith(emitSlow)
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

        Observable.merge(arrayListOf(emitFast, emitSlow))
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Like merge but on error, it stops only the failed source
//
//    D: fast 0
//    D: fast 1
//    D: slow 0
//    D: fast 2
//    D: fast 3
//    D: fast 4
//    D: slow 1
//    D: fast 5
//    D: fast 6 <- 7 is missing and there is no more emission from fast
//    D: slow 2
//    D: slow 3
//    D: slow 4
//    D: slow 5
//    D: slow 6
//    D: slow 7
    fun mergeDelayError() {
        val emitFast = Observable.interval(0, 300, TimeUnit.MILLISECONDS).map {
            if (it == 7L) throw IllegalStateException("random error")
            "fast $it"
        }
        val emitSlow = Observable.interval(400, 900, TimeUnit.MILLISECONDS).map { "slow $it" }
        Observable.mergeDelayError(arrayListOf(emitFast, emitSlow))
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Puts the emission of a source before another
//
//    D: slow 0
//    D: slow 1
//    D: slow 2
//    D: slow 3
//    D: slow 4
//    D: fast 0
//    D: fast 1
//    D: fast 2
//    D: fast 3
//    D: fast 4
    fun startWith() {
        val emitFast = Observable.interval(0, 300, TimeUnit.MILLISECONDS).map { "fast $it" }
        val emitSlow = Observable.interval(400, 900, TimeUnit.MILLISECONDS).map { "slow $it" }.take(5)
        emitFast.startWith(emitSlow)
            .take(10)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Flattens embedded observable emissions
//
//    D: outer: 0 - inner: 0
//    D: outer: 0 - inner: 1
//    D: outer: 0 - inner: 2
//    D: outer: 0 - inner: 3
//    D: outer: 0 - inner: 4
//    D: outer: 0 - inner: 5
//    D: outer: 0 - inner: 6
//    D: outer: 0 - inner: 7
//    D: outer: 0 - inner: 8
//    D: outer: 1 - inner: 0
//    D: outer: 1 - inner: 1
//    D: outer: 1 - inner: 2
//    D: outer: 1 - inner: 3
//    D: outer: 1 - inner: 4
//    D: outer: 1 - inner: 5
    fun switchOnNext() {
        val timeIntervals = Observable.interval(1, TimeUnit.SECONDS)
            .map { ticks ->
                    Observable.interval(100, TimeUnit.MILLISECONDS).map { innerInterval -> "outer: $ticks - inner: $innerInterval" }
                }

        Observable.switchOnNext(timeIntervals)
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Simimilar to combineLatest but this one only emits if all the sources emitted a new value
//
//    D: fast: 0, slow: 0
//    D: fast: 1, slow: 1
//    D: fast: 2, slow: 2
//    D: fast: 3, slow: 3
//    D: fast: 4, slow: 4
//    D: fast: 5, slow: 5
//    D: fast: 6, slow: 6
//    D: fast: 7, slow: 7
//    D: fast: 8, slow: 8
//    D: fast: 9, slow: 9
//    D: fast: 10, slow: 10
//    D: fast: 11, slow: 11
//    D: fast: 12, slow: 12
//    D: fast: 13, slow: 13
//    D: fast: 14, slow: 14
    fun zip() {
        val emitFast = Observable.interval(0, 300, TimeUnit.MILLISECONDS)
        val emitSlow = Observable.interval(400, 900, TimeUnit.MILLISECONDS)
        //no matter what the lint say it won't compile without the implicit generics and function declaration, rxkotlin solves the issue
        Observable.zip<Long, Long, String>(emitFast, emitSlow, BiFunction { fastEmission, slowEmission ->
            "fast: $fastEmission, slow: $slowEmission"
        })
            .take(15)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

    fun latest() {

    }
}