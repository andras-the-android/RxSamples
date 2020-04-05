package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@SuppressLint("CheckResult")
class Transformation {

//    It collects the items from the upstream into lists with the given size
//    D: [0, 1, 2, 3]
//    D: [4, 5, 6, 7]
//    D: [8, 9]
//    D: completed
    fun buffer() {
        Observable.range(0, 10)
            .buffer(4)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }



    fun map() {

//        D: mapped value: 1
//        D: mapped value: 2
//        D: mapped value: 3
        Observable.just(1, 2, 3)
            .map { "mapped value: $it" }
            .subscribe { log(it)}

    }

    fun flatMap() {

//   flatMap creates 0 or more item from one. Typical usage is transformation of Observable<Collection> into Observable<CollectionItem>

//        Don't forget that flatMap doesn't guarantees the original order. If you uncomment the delay(...), the order of the results will be uncertain!
//
//        D: transformed 1
//        D: transformed 2
//        D: transformed 3
//        D: transformed 7
//        D: transformed 8
//        D: transformed 9
//        D: transformed 4
//        D: transformed 5
//        D: transformed 6
        Observable.just(arrayOf(1, 2, 3), arrayOf(4, 5, 6), arrayOf(7, 8, 9))
            .flatMap {
                Observable.create { emitter: ObservableEmitter<String> ->
                    it.iterator().forEach {arrayItem -> emitter.onNext("transformed $arrayItem" ) }
                    emitter.onComplete()
                }
//                .delay(Random(628282929).nextLong(1000), TimeUnit.MILLISECONDS)
            }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

    fun concatMap() {

//        This is the same as flatMap except it keeps the original order of the items. In exchange it's slower.
//
//        D: transformed 1
//        D: transformed 2
//        D: transformed 3
//        D: transformed 4
//        D: transformed 5
//        D: transformed 6
//        D: transformed 7
//        D: transformed 8
//        D: transformed 9
        Observable.just(arrayOf(1, 2, 3), arrayOf(4, 5, 6), arrayOf(7, 8, 9))
            .concatMap {
                Observable.create { emitter: ObservableEmitter<String> ->
                    it.iterator().forEach {arrayItem -> emitter.onNext("transformed $arrayItem" ) }
                    emitter.onComplete()
                }
                .delay(Random(628282929).nextLong(1000), TimeUnit.MILLISECONDS)
            }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Cuts the emission of the upstream into groups (observables) by the keySelector parameter. There is a value selector param which can transform the value,
//    but i think a separate map transformation is more expressive.
//
//    D: [Tiger, turtle]
//    D: [Elephant]
//    D: [Cat, chameleon]
//    D: [Frog, Fish, Flamingo]
//    D: completed
    fun groupBy() {
        Observable.just("Tiger", "Elephant", "Cat", "chameleon", "Frog", "fish", "turtle", "Flamingo")
            .groupBy { it[0].toLowerCase() }
            .concatMapSingle { it.toList() }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    accumulates the emission of the upstream. It's worth to mention that there is a 0th emission which is the initial value
//    D: 1
//    D: 6
//    D: 9
//    D: 17
//    D: 18
//    D: 25
//    D: completed
    fun scan() {
        Observable.just(5, 3, 8, 1, 7)
            .scan(1, {partialSum, x -> partialSum + x })
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Every time the upstream emits, it creates a new observable and make the downstream subscribe to it.
//    Here the source emits once in a second, so every observable created by the switchMap can emit at most twice
//    because it's delay is 750ms.
//
//    D: observable #0: 0
//    D: observable #0: 1
//    D: observable #1: 0
//    D: observable #1: 1
//    D: observable #2: 0
//    D: observable #2: 1
//    D: observable #3: 0
//    D: completed
    fun switchMap() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .switchMap { sourceValue ->
                Observable.interval(0, 750, TimeUnit.MILLISECONDS).map { "observable #$sourceValue: $it" }
            }
            .take(7)
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Slices small subsequences from the upstream and puts them into new observables
//
//    D: [1, 2]
//    D: [4, 5]
//    D: [7, 8]
//    D: [10]
//    D: completed
    fun window() {
        Observable.range(1, 10)
            // Create slices containing at most 2 items, and skip 3 items before starting a new window.
            .window(2, 3)
            .flatMapSingle { it.toList() }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

    fun latest() {

    }

}