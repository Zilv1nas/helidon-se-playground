package com.github.zilv1nas.config.tracing

import com.github.zilv1nas.config.tracing.Tracing.register
import io.helidon.tracing.HeaderConsumer
import io.helidon.tracing.Span
import io.helidon.tracing.SpanContext
import io.helidon.tracing.Tag

interface WithMessagingConsumerTracing {
    fun trace(channelName: String, spanContext: SpanContext?): Span {
        val tracer = Tracing.tracer()

        val spanBuilder = tracer.spanBuilder("consume event from $channelName")
        spanContext?.run {
            spanBuilder.parent(this)
        }

        val span = spanBuilder.start()
        span.tag(Tag.create("component", "event consumer"))
        span.tag(Tag.create("messaging.destination", channelName))
        span.tag(Tag.create("messaging.system", "kafka"))
        span.tag(Tag.create("span.kind", "consumer"))

        span.context().register()

        return span
    }

    @Suppress("UNCHECKED_CAST")
    fun extractTracing(metadata: Map<String, Any>): SpanContext? {
        val headers = metadata.mapValues { (_, value) ->
            value.takeIf { it is List<*> }?.let { it as? List<String> }
        }.toMutableMap()

        val spanContext = Tracing.tracer().extract(HeaderConsumer.create(headers)).orElse(null)
        spanContext?.register()

        return spanContext
    }
}