package com.github.zilv1nas.config

import io.helidon.config.Config
import org.flywaydb.core.Flyway

class FlywayConfig(private val config: Config) {
    fun runMigrations() {
        val dbConfig = config["db.connection"]

        val flyway = Flyway
            .configure()
            .dataSource(
                dbConfig["url"].asString().get(),
                dbConfig["username"].asString().get(),
                dbConfig["password"].asString().get()
            )
            .locations(config["flyway.location"].asString().get())
            .load()

        flyway.info()
        flyway.migrate()
    }
}