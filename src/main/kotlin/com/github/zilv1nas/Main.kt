package com.github.zilv1nas

import com.github.zilv1nas.config.AppWebServer
import com.github.zilv1nas.config.FlywayConfig
import com.github.zilv1nas.config.KoinConfig
import org.slf4j.bridge.SLF4JBridgeHandler

fun main() {
    setupSLF4JLogs()

    val koin = KoinConfig.configure().koin
    koin.get<FlywayConfig>().runMigrations()
    koin.get<AppWebServer>().start()
}

private fun setupSLF4JLogs() {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
}