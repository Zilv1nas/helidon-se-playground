package com.github.zilv1nas.service

import com.github.zilv1nas.config.tracing.WithMessagingProducerTracing
import com.github.zilv1nas.service.model.Event
import com.github.zilv1nas.service.model.EventEnvelope
import io.helidon.common.reactive.Single
import io.helidon.messaging.Channel
import io.helidon.messaging.Emitter
import io.helidon.tracing.SpanContext
import org.eclipse.microprofile.reactive.messaging.Message
import org.slf4j.Logger
import java.util.concurrent.CompletableFuture

abstract class EventProducer<E : Event>(
    channelName: String,
    protected val logger: Logger,
) : WithMessagingProducerTracing {
    private val channel: Channel<EventEnvelope<E>> = Channel.create(channelName)
    val emitter: Emitter<EventEnvelope<E>> = Emitter.create(channel)

    fun publish(event: E, spanContext: SpanContext? = null): Single<Void> {
        logger.info("Publishing event: $event")
        val (onTraceSuccess, onTraceFailure) = trace(channel.name(), spanContext)
        val eventEnvelope = EventEnvelope.create(event, injectTracing(spanContext))

        val resultFuture = CompletableFuture<Void>()
        val message = Message.of(
            eventEnvelope,
            {
                onTraceSuccess()
                resultFuture.complete(null)
                CompletableFuture.completedFuture(null)
            },
            { throwable ->
                onTraceFailure(throwable)
                resultFuture.completeExceptionally(throwable)
                CompletableFuture.completedFuture(null)
            },
        )
        emitter.send(message)
        return Single.create(resultFuture, true)
    }
}