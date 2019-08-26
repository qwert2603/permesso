package com.qwert2603.permesso_coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import org.junit.Test

import java.util.concurrent.Executors

@FlowPreview
@ExperimentalCoroutinesApi
class ExampleUnitTest {
    @Test
    fun f() = runBlocking<Unit> {
        val receiveChannel = ch()
        val broadcastChannel = br()

        launch {
            delay(1500)
            val openSubscription = broadcastChannel.openSubscription()
            for (ch in openSubscription) {
                println("a $ch")
                if (ch == 7) openSubscription.cancel()
            }
        }
        launch {
            delay(2500)
            val openSubscription = broadcastChannel.openSubscription()
            for (ch in openSubscription) {
                println("${Thread.currentThread()} b $ch")
                if (ch == 5) openSubscription.cancel()
            }
        }
        launch {
            delay(3500)
            broadcastChannel.asFlow()
                    .onEach {  println("${Thread.currentThread()} onEach $it") }
                    .flowOn(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
                    .collect { println("${Thread.currentThread()} collect $it") }
        }
    }

    private fun CoroutineScope.ch(): ReceiveChannel<Int> = produce {
        repeat(10) {
            send(it)
            delay(300)
        }
    }

    private fun CoroutineScope.br(): BroadcastChannel<Int> = broadcast(
            capacity = Channel.CONFLATED,
            start = CoroutineStart.DEFAULT
    ) {
        repeat(10) {
            send(it)
            delay(1000)
        }
        channel.invokeOnClose {
            println("invokeOnClose")
        }
    }
}
