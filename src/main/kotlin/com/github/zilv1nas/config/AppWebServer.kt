package com.github.zilv1nas.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.helidon.config.Config
import io.helidon.media.jackson.JacksonSupport
import io.helidon.tracing.TracerBuilder
import io.helidon.webserver.Routing
import io.helidon.webserver.Service
import io.helidon.webserver.WebServer
import io.helidon.webserver.WebTracingConfig
import io.helidon.webserver.accesslog.AccessLogSupport

class AppWebServer(
    private val config: Config,
    private val objectMapper: ObjectMapper,
    private val apis: List<Service>,
    private val initializingComponents: List<InitializingComponent>,
) {
    fun start() {
        initializingComponents.forEach {
            it.start()
        }

        val routing = Routing.builder()
            .register(AccessLogSupport.create(config.get("server.access-log")))
            .register(WebTracingConfig.create(config.get("tracing")))
            .register(*apis.toTypedArray())
            .build()

        WebServer.builder(routing)
            .config(config.get("server"))
            .addMediaSupport(JacksonSupport.create(objectMapper))
            .tracer(TracerBuilder.create(config.get("tracing")).build())
            .build()
            .start()
            .thenAccept { server ->
                server.whenShutdown().thenRun {
                    initializingComponents.forEach { it.stop() }
                }
            }
    }
}