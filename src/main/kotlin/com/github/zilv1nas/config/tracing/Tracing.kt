package com.github.zilv1nas.config.tracing

import io.helidon.common.context.Context
import io.helidon.common.context.Contexts
import io.helidon.tracing.SpanContext
import io.helidon.tracing.Tracer

object Tracing {
    fun Context.tracer(): Tracer = get(Tracer::class.java).orElseGet(Tracer::global)

    fun Context.spanContext(): SpanContext? = get(SpanContext::class.java).orElse(null)

    fun SpanContext.register() {
        Contexts.context()
            .orElseGet(Context::create)
            .register(this)
    }

    fun tracer(): Tracer = Contexts.context().orElse(null)?.tracer() ?: Tracer.global()
}