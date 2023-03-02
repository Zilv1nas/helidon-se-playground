package com.github.zilv1nas.service

import com.github.zilv1nas.config.tracing.WithMessagingConsumerTracing
import com.github.zilv1nas.service.model.Event
import com.github.zilv1nas.service.model.EventEnvelope
import io.helidon.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams
import org.eclipse.microprofile.reactive.streams.operators.SubscriberBuilder
import org.slf4j.Logger

abstract class EventConsumer<E : Event>(
    channelName: String,
    protected val logger: Logger,
) : WithMessagingConsumerTracing {
    val channel: Channel<EventEnvelope<E>> = Channel.create(channelName)

    val listener: SubscriberBuilder<Message<EventEnvelope<E>>, Void> =
        ReactiveStreams.builder<Message<EventEnvelope<E>>>().forEach { message ->
            val event = message.payload.data
            logger.info("Consuming event: $event")

            val spanContext = extractTracing(message.payload.metadata)
            val span = trace(channel.name(), spanContext)

            try {
                on(event)
                message.ack()
                span.end()
            } catch (e: Throwable) {
                logger.error("Could not consume event $event", e)
                span.end(e)
                throw e
            }
        }

    protected abstract fun on(event: E)
}