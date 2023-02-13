package com.github.zilv1nas.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.zilv1nas.web.UsersApi
import io.helidon.config.Config
import io.helidon.media.jackson.JacksonSupport
import io.helidon.webserver.Routing
import io.helidon.webserver.WebServer
import io.helidon.webserver.accesslog.AccessLogSupport
import java.net.InetAddress

class AppWebServer(private val config: Config, private val usersApi: UsersApi) {
    fun start() {
        val routing = Routing.builder()
            .register(AccessLogSupport.create(config.get("server.access-log")))
            .register(usersApi)
            .build()

        WebServer.builder(routing)
            .config(config.get("server"))
            .bindAddress(InetAddress.getLocalHost())
            .port(8080)
            .addMediaSupport(JacksonSupport.create(createObjectMapper()))
            .build()
            .start()
    }

    private fun createObjectMapper() = ObjectMapper()
        .registerModule(ParameterNamesModule())
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule())
        .registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
}