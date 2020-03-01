package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@SuppressLint("CheckResult")
class Transformation {


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

    fun latest() {

    }

}