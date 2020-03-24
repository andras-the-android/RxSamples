package com.example.rxsamples

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@SuppressLint("CheckResult")
class Multicast {


//    D: start
//    D: connect
//    D: onnext1 0
//    D: onnext1 1
//    D: onnext1 2
//    D: onnext1 3
//    D: onnext1 4
//    D: onnext1 5
//    D: onnext1 6
//    D: onnext2 6
//    D: onnext1 7
//    D: onnext2 7
//    D: onnext1 8
//    D: onnext2 8
//    D: onnext1 9
//    D: onnext2 9
//    D: onnext1 10
//    D: onnext2 10
//    D: onnext1 11
//    D: onnext2 11
//    D: onnext1 12
//    D: onnext2 12
//    D: onnext1 13
//    D: onnext2 13
//    D: onnext1 14
//    D: onnext2 14
//    D: onnext1 15
//    D: onnext2 15
//    D: onnext2 16
//    D: onnext2 17
//    D: onnext2 18
//    D: onnext2 19
//    D: onnext2 20
//    D: onnext2 21
    fun hot1() {
        log("start")
        //a regular observer
        val myObservable: Observable<Long> = Observable.interval(500, TimeUnit.MILLISECONDS)
        //the publish() call converts it into a hot observer
        val connectableObservable: ConnectableObservable<Long> = myObservable.publish()

        val disposable1 = connectableObservable.subscribe( { log("onnext1 $it") }, { log(it.message ?: "empty error message") }, { log("completed1") })
        Thread.sleep(3000)

        log("connect")
        //emission starts with the connect() call despite the first subscriber is present seconds before
        val connectDisposable = connectableObservable.connect()
        Thread.sleep(3000)

        connectableObservable.subscribe( { log("onnext2 $it") }, { log(it.message ?: "empty error message") }, { log("completed2") })
        Thread.sleep(5000)

        disposable1.dispose()
        Thread.sleep(3000)

        connectDisposable.dispose()
    }

//    Observe the timestamps! Notice that even though the subscription is at 5000ms the first value is at 6000ms. There is no cached value here
//
//    2020-03-17 00:03:15.075 D: start
//    2020-03-17 00:03:21.097 D: onnext 1
//    2020-03-17 00:03:24.097 D: onnext 2
//    2020-03-17 00:03:27.096 D: onnext 3
    fun hot2() {
        log("start")
        val myObservable: Observable<Long> = Observable.interval(3000, TimeUnit.MILLISECONDS)
        val connectableObservable: ConnectableObservable<Long> = myObservable.publish()

        val connectDisposable = connectableObservable.connect()
        Thread.sleep(5000)

        connectableObservable.subscribe( { log("onnext $it") }, { log(it.message ?: "empty error message") }, { log("completed") })
        Thread.sleep(7000)

        connectDisposable.dispose()
    }

//    Multiple connect calls doesn't affect the source
//
//    D/xxxx: start
//    D/xxxx: onnext 0
//    D/xxxx: onnext 1
//    D/xxxx: onnext 2
//    D/xxxx: onnext 3
//    D/xxxx: onnext 4
//    D/xxxx: onnext 5
//    D/xxxx: onnext 6
//    D/xxxx: onnext 7
    fun repeatedConnect() {
        log("start")
        val myObservable: Observable<Long> = Observable.interval(500, TimeUnit.MILLISECONDS)
        val connectableObservable: ConnectableObservable<Long> = myObservable.publish()

        connectableObservable.connect()
        connectableObservable.subscribe( { log("onnext $it") }, { log(it.message ?: "empty error message") }, { log("completed") })
        Thread.sleep(2000)

        val connectDisposable = connectableObservable.connect()
        Thread.sleep(2000)

        connectDisposable.dispose()
    }

//    In case of error all the subscribers will get onError
//
//    D: onnext1 0
//    D: onnext2 0
//    D: onnext1 1
//    D: onnext2 1
//    D: onnext1 2
//    D: onnext2 2
//    D: onnext1 3
//    D: onnext2 3
//    D: onnext1 4
//    D: onnext2 4
//    D: empty error message
//    D: empty error message
    fun error() {
        log("start")
        val myObservable: Observable<Long> = Observable.interval(500, TimeUnit.MILLISECONDS).map { if (it == 5L) throw TimeoutException() else it }
        val connectableObservable: ConnectableObservable<Long> = myObservable.publish()
        connectableObservable.subscribe( { log("onnext1 $it") }, { log(it.message ?: "empty error message") }, { log("completed1") })
        connectableObservable.subscribe( { log("onnext2 $it") }, { log(it.message ?: "empty error message") }, { log("completed2") })
        connectableObservable.connect()
    }

//    2020-03-17 00:28:46.388 D: start
//    2020-03-17 00:28:47.894 D: onnext1 0
//    2020-03-17 00:28:48.393 D: onnext1 1
//    2020-03-17 00:28:48.893 D: onnext1 2
//    2020-03-17 00:28:49.393 D: onnext1 3
//    2020-03-17 00:28:49.894 D: onnext1 4
//    2020-03-17 00:28:50.399 D: onnext1 5
//    2020-03-17 00:28:50.899 D: onnext1 6
//    2020-03-17 00:28:50.900 D: onnext2 6
//    2020-03-17 00:28:51.393 D: onnext1 7
//    2020-03-17 00:28:51.394 D: onnext2 7
//    2020-03-17 00:28:51.893 D: onnext1 8
//    2020-03-17 00:28:51.894 D: onnext2 8
//    2020-03-17 00:28:52.393 D: onnext1 9
//    2020-03-17 00:28:52.394 D: onnext2 9
//    2020-03-17 00:28:52.899 D: onnext1 10
//    2020-03-17 00:28:52.899 D: onnext2 10
//    2020-03-17 00:28:53.393 D: onnext1 11
//    2020-03-17 00:28:53.393 D: onnext2 11
//    2020-03-17 00:28:53.897 D: onnext2 12
//    2020-03-17 00:28:54.393 D: onnext2 13
//    2020-03-17 00:28:54.893 D: onnext2 14
//    2020-03-17 00:28:55.393 D: onnext2 15
//    2020-03-17 00:28:55.893 D: onnext2 16
//    2020-03-17 00:28:56.399 D: onnext2 17
//    2020-03-17 00:28:56.906 D: onnext3 0
//    2020-03-17 00:28:57.411 D: onnext3 1
//    2020-03-17 00:28:57.911 D: onnext3 2
//    2020-03-17 00:28:58.406 D: onnext3 3
//    2020-03-17 00:28:58.911 D: onnext3 4
    fun refCount() {
        log("start")
        //a regular observer
        val myObservable: Observable<Long> = Observable.interval(500, TimeUnit.MILLISECONDS)
        //the publish().refCount call converts it into a cold multicast observer which start emitting on the first subscriber and completes
        //when the last subscriber disposes but shares it observable source among them
        val multicastObservable: Observable<Long> = myObservable.publish().refCount()
        Thread.sleep(1000)

        val disposable1 = multicastObservable.subscribe( { log("onnext1 $it") }, { log(it.message ?: "empty error message") }, { log("completed1") })
        Thread.sleep(3000)

        val disposable2 = multicastObservable.subscribe({ log("onnext2 $it") }, { log(it.message ?: "empty error message") }, { log("completed2") })
        Thread.sleep(3000)

        disposable1.dispose()
        Thread.sleep(3000)

        disposable2.dispose()

        //the counter will start from the beginning
        val disposable3 = multicastObservable.subscribe({ log("onnext3 $it") }, { log(it.message ?: "empty error message") }, { log("completed3") })
        Thread.sleep(3000)

        disposable3.dispose()
    }

//    If I'd replace replay with publish, the first item would be 4 because 3 was emitted before the subscription. As you can see the first item is emitted closer to the second
//    then the the defined period because it's from the cache. There is only one item because is the defined buffer size in the replay() call.
//
//    2020-03-23 19:28:20.206 D: start
//    2020-03-23 19:28:22.629 D: onnext1 3
//    2020-03-23 19:28:22.727 D: onnext1 4
//    2020-03-23 19:28:23.228 D: onnext1 5
//    2020-03-23 19:28:23.729 D: onnext1 6
//    2020-03-23 19:28:24.228 D: onnext1 7
//    2020-03-23 19:28:24.729 D: onnext1 8
//    2020-03-23 19:28:25.227 D: onnext1 9
    fun replay() {
        log("start")
        val myObservable: Observable<Long> = Observable.interval(500, TimeUnit.MILLISECONDS)
        val connectableObservable: ConnectableObservable<Long> = myObservable.replay(1)
        val disposable = connectableObservable.connect()
        Thread.sleep(2400)
        connectableObservable.subscribe( { log("onnext1 $it") }, { log(it.message ?: "empty error message") }, { log("completed1") })
        Thread.sleep(3000)
        disposable.dispose()
    }

    fun latest() {
    }
}