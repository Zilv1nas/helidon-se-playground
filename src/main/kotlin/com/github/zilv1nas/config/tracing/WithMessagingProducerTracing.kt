package com.github.zilv1nas.config.tracing

import io.helidon.tracing.HeaderConsumer
import io.helidon.tracing.HeaderProvider
import io.helidon.tracing.Span
import io.helidon.tracing.SpanContext
import io.helidon.tracing.Tag

interface WithMessagingProducerTracing {
    fun injectTracing(spanContext: SpanContext?): Map<String, List<String>> {
        if (spanContext == null) {
            return emptyMap()
        }

        val traceHeaders = mutableMapOf<String, List<String>>()
        Tracing.tracer().inject(spanContext, HeaderProvider.empty(), HeaderConsumer.create(traceHeaders))
        return traceHeaders
    }

    fun trace(channelName: String, spanContext: SpanContext?): Span {
        val tracer = Tracing.tracer()

        val spanBuilder = tracer.spanBuilder("produce event to $channelName")
        spanContext?.run {
            spanBuilder.parent(this)
        }

        val span = spanBuilder.start()
        span.tag(Tag.create("component", "event producer"))
        span.tag(Tag.create("messaging.destination", channelName))
        span.tag(Tag.create("messaging.system", "kafka"))
        span.tag(Tag.create("span.kind", "producer"))

        return span
    }
}