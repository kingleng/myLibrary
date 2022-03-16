package com.cpsdna.app.mykotlinapplication.test

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 *
 * Created by leng on 2019/10/16.
 */
//fun main(args: Array<String>) = runBlocking<Unit> {
//    val job = launch { doWorld() }
//    println("Hello,")
//    job.join()
//}
//
//// 这是你第一个挂起函数
//suspend fun doWorld() {
//    delay(1000L)
//    println("World!")
//}

fun main(args: Array<String>) = runBlocking<Unit> {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}