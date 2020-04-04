package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("CheckResult")
class Aggregation {

//    Summarises the emissions into one single emission
//
//    D: sum 1 item 2
//    D: sum 3 item 3
//    D: sum 6 item 4
//    D: sum 10 item 5
//    D: 15
    fun reduce() {
        Observable.range(1, 5)
            .reduce { sum, item -> sum + item.also { log("sum $sum item $item") } }
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

    }

//    Like the plain reduce but here you need to specify the summary object
//
//    D: [1, 2, 3, 4, 5]
    fun _reduceWith() {
        Observable.range(1, 5)
            .reduceWith( { ArrayList<Int>() }, { sum, item -> sum.add(item); return@reduceWith sum })
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

//    Similar to reduceWidth but you don't need to return from the collector block
//
//    D: [1, 2, 3, 4, 5]
    fun collect() {
        Observable.range(1, 5)
            .collect( { ArrayList<Int>() }, { sum, item -> sum.add(item) })
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })

        //same as collect but you don't need a lambda expression to create the collector object
        Observable.range(1, 5)
            .collectInto(ArrayList<Int>(), { sum, item -> sum.add(item) })
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

    fun toList() {
//        D: [1, 2, 3, 4, 5]
        Observable.range(1, 5)
            .toList()
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })

//        D: [5, 4, 3, 2, 1]
        Observable.range(1, 5)
            .toSortedList { o1, o2 -> o2 - o1 } //standard java comparator
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

    fun toMap() {
//        The value selector defines the value in the map
//
//        D: {f=FLAMINGO, e=ELEPHANT, c=CHAMELEON, t=TURTLE}
        Observable.just("Tiger", "Elephant", "Cat", "chameleon", "Frog", "fish", "turtle", "Flamingo")
            .toMap({ it[0].toLowerCase() }, { it.toUpperCase(Locale.getDefault()) })
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })

//        The value in the result map is a collection and the result of the value selector is added to this collection

//        D: {f=[FROG, FISH, FLAMINGO], e=[ELEPHANT], c=[CAT, CHAMELEON], t=[TIGER, TURTLE]}
        Observable.just("Tiger", "Elephant", "Cat", "chameleon", "Frog", "fish", "turtle", "Flamingo")
            .toMultimap({ it[0].toLowerCase() }, { it.toUpperCase(Locale.getDefault()) })
            .subscribe({ log(it.toString()) }, { log(it.message ?: "empty error message") })
    }

    fun latest() {

    }
}