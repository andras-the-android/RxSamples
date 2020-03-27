package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.ofType
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class Filtering {


//    only emit an item from an Observable if a particular timespan has passed without it emitting another item
////    Here as you can see all the items are filtered out where the sleep time after is less then 100ms
////
////    D: 1
////    D: 3
////    D: 4
    fun debounce() {
        Observable.create<Int> {
            it.onNext(0)
            Thread.sleep(50)
            it.onNext(1)
            Thread.sleep(150)
            it.onNext(2)
            Thread.sleep(50)
            it.onNext(3)
            Thread.sleep(150)
            it.onNext(4)
            it.onComplete()
        }
            .debounce(100, TimeUnit.MILLISECONDS)
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    Removes duplications no matter where they are
//
//    D: 2
//    D: 3
//    D: 4
//    D: 1
    fun distinct() {
        Observable.just(2, 3, 4, 4, 2, 1)
            .distinct()
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

        //the result is the same as at the first one. The key selector makes possible to extract a part from a complex object
        // to make it the basis of the distinction
        Observable.just(Pair(2, "aaaa"), Pair(3, "aaaa"), Pair(4, "aaaa"), Pair(4, "aaaa"), Pair(2, "aaaa"), Pair(1, "aaaa"))
            .distinct { it.first }
            .subscribe( { log(it.first.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//    similar to distinct but it removes the duplicates only if they follow each other
//    D: 1
//    D: 2
//    D: 1
//    D: 2
//    D: 3
//    D: 4
    fun distinctUntilChanged() {
        Observable.just(1, 1, 2, 1, 2, 3, 3, 4)
            .distinctUntilChanged()
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })
    }

//  takes the item with the selected index (starts at 0) or a default value if the index is'n present
    fun elementAt() {
//        D: 6
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .elementAt(5)
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") }, { log("completed") })

//        D: 50
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .elementAt(11, 50)
            .subscribe( { log(it.toString()) }, { log(it.message ?: "empty error message") })

//        D: java.util.NoSuchElementException
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .elementAtOrError(11)
                .subscribe( { log(it.toString()) }, { log(it.toString()) })
    }

//    D: 1
//    D: 2
//    D: 3
//    D: 4
    fun filter() {
        Observable.just(1, 2, 3, 4, 5, 6, 7L, 8, 9)
            .filter { it <  5}
            .subscribe( { log(it.toString()) }, { log(it.toString()) })
    }

//    Converts Maybe, Single, Observable to Completable
    fun ignore() {
        Single.just(1)
            .ignoreElement()
            .subscribe( { log("completed") }, { log(it.toString()) })

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .ignoreElements()
            .subscribe( { log("completed") }, { log(it.toString()) })

    }

//    Filters by class
//    D: 2
//    D: 7
    fun ofType() {
        Observable.just(1, "2", 3, 4, 5, 6, "7", 8, 9)
            .ofType(String::class.java)
            .subscribe( { log(it) }, { log(it.toString()) })
    }


//    Passes through the item which is the closest in time to the end of the given time period
//    D: C
//    D: D
    fun sample() {
        Observable.create<String> { emitter ->
            emitter.onNext("A") //0ms

            Thread.sleep(500)
            emitter.onNext("B") //500ms

            Thread.sleep(200)
            emitter.onNext("C") //700ms

            Thread.sleep(800)
            emitter.onNext("D") //1500ms

            Thread.sleep(600)
            emitter.onNext("E") //2100ms
            emitter.onComplete();
        }
            .sample(1, TimeUnit.SECONDS)
            .subscribe( { log(it) }, { log(it.toString()) })
    }

    fun take() {
//        D: 1
//        D: 2
//        D: 3
        Observable.just(1, 2, 3, 4, 5, 6, 2, 8, 9)
            .take(3)
            .subscribe( { log(it.toString()) }, { log(it.toString()) })

//        D: 7
//        D: 8
//        D: 9
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .takeLast(3)
            .subscribe( { log(it.toString()) }, { log(it.toString()) })
    }

//    Passes through the item which is the closest in time to the beginning of the given time period
//    D: A
//    D: D
    fun throttleFirst() {
        Observable.create<String> { emitter ->
            emitter.onNext("A") //0ms

            Thread.sleep(500)
            emitter.onNext("B") //500ms

            Thread.sleep(200)
            emitter.onNext("C") //700ms

            Thread.sleep(800)
            emitter.onNext("D") //1500ms

            Thread.sleep(600)
            emitter.onNext("E") //2100ms
            emitter.onComplete();
        }
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe( { log(it) }, { log(it.toString()) })
    }

//    Same as Sample. Passes through the item which is the closest in time to the end of the given time period
//    D: C
//    D: D
    fun throttleLast() {
        Observable.create<String> { emitter ->
            emitter.onNext("A") //0ms

            Thread.sleep(500)
            emitter.onNext("B") //500ms

            Thread.sleep(200)
            emitter.onNext("C") //700ms

            Thread.sleep(800)
            emitter.onNext("D") //1500ms

            Thread.sleep(600)
            emitter.onNext("E") //2100ms
            emitter.onComplete();
        }
            .throttleLast(1, TimeUnit.SECONDS)
            .subscribe( { log(it) }, { log(it.toString()) })
    }

//    Same as throttleLast except that it always emmits the first value and the timer starts from there
//    D: A
//    D: C
//    D: D
    fun throttleLatest() {
        Observable.create<String> { emitter ->
            Thread.sleep(600)
            emitter.onNext("A") //0ms

            Thread.sleep(500)
            emitter.onNext("B") //500ms

            Thread.sleep(200)
            emitter.onNext("C") //700ms

            Thread.sleep(800)
            emitter.onNext("D") //1500ms

            Thread.sleep(600)
            emitter.onNext("E") //2100ms
            emitter.onComplete();
        }
            .throttleLatest(1, TimeUnit.SECONDS)
            .subscribe( { log(it) }, { log(it.toString()) })
    }

    fun latest() {

    }
}