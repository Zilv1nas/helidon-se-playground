package com.github.zilv1nas.config.tracing

import com.github.zilv1nas.config.tracing.Tracing.spanContext
import com.github.zilv1nas.config.tracing.Tracing.tracer
import io.helidon.common.reactive.Single
import io.helidon.dbclient.DbClientServiceContext
import io.helidon.dbclient.common.DbClientServiceBase
import io.helidon.tracing.Tag
import io.helidon.common.Builder as HelidonBuilder

class DbClientTracing private constructor(builder: Builder) : DbClientServiceBase(builder) {
    override fun apply(serviceContext: DbClientServiceContext): Single<DbClientServiceContext> {
        val context = serviceContext.context()
        val tracer = context.tracer()

        val spanBuilder = tracer.spanBuilder("database call")
        context.spanContext()?.run {
            spanBuilder.parent(this)
        }

        val span = spanBuilder.start()
        span.tag(Tag.create("component", "dbclient"))
        span.tag(Tag.create("db.type", serviceContext.dbType()))
        span.tag(Tag.create("db.operation", serviceContext.statementType().toString()))
        span.tag(Tag.create("db.statement", serviceContext.statement()))

        serviceContext.statementFuture().thenAccept {
            span.addEvent("statement-finish")
        }
        serviceContext.resultFuture().thenAccept { count ->
            span.addEvent("result-finish", mapOf("count" to count))
            span.end()
        }.exceptionally { throwable ->
            span.end(throwable)
            null
        }
        return Single.just(serviceContext)
    }

    companion object {
        fun create() = Builder().build()
    }

    class Builder : DbClientServiceBuilderBase<Builder>(),
        HelidonBuilder<Builder, DbClientTracing> {

        override fun build(): DbClientTracing {
            return DbClientTracing(this)
        }
    }
}
