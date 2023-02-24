package com.github.zilv1nas.config

import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import io.helidon.dbclient.health.DbClientHealthCheck
import io.helidon.health.HealthSupport
import io.helidon.health.checks.HealthChecks

object HealthConfig {
    fun configure(config: Config, dbClient: DbClient): HealthSupport = HealthSupport.builder()
        .addLiveness(*HealthChecks.healthChecks())
        .addReadiness(DbClientHealthCheck.create(dbClient, config["db.health"]))
        .build()
}