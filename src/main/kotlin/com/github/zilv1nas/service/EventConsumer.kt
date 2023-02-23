package com.github.zilv1nas.service

import com.github.zilv1nas.service.model.Event
import io.helidon.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams
import org.eclipse.microprofile.reactive.streams.operators.SubscriberBuilder
import org.slf4j.Logger

abstract class EventConsumer<E : Event> {
    protected abstract val logger: Logger

    abstract val channel: Channel<E>

    val listener: SubscriberBuilder<Message<E>, Void> = ReactiveStreams.builder<Message<E>>()
        .onError {
            println(it)
        }
        .forEach {
            val event = it.payload
            logger.info("Consuming event: $event")

            try {
                on(it.payload)
            } catch (e: Throwable) {
                logger.error("Could not consume event $event", e)
                throw e
            }
            it.ack()
        }

    protected abstract fun on(event: E)
}