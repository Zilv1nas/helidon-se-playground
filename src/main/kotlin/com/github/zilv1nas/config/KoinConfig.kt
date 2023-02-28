package com.github.zilv1nas.config

import com.github.zilv1nas.config.serialization.ObjectMapperFactory
import com.github.zilv1nas.config.tracing.DbClientTracing
import com.github.zilv1nas.repository.UsersRepository
import com.github.zilv1nas.service.EventConsumer
import com.github.zilv1nas.service.EventProducer
import com.github.zilv1nas.service.UserEventsProducer
import com.github.zilv1nas.service.UsersProjector
import com.github.zilv1nas.service.model.UserCreated
import com.github.zilv1nas.web.UsersApi
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import io.helidon.webserver.Service
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

object KoinConfig {
    fun configure(): KoinApplication {
        val appModule = module {
            single { Config.create() }

            single { ObjectMapperFactory.create() }

            single {
                DbClient.builder()
                    .config(get<Config>().get("db"))
                    .addService(DbClientTracing.create())
                    .build()
            }

            single {
                AppWebServer(
                    config = get(),
                    objectMapper = get(),
                    apis = getAll(),
                    initializingComponents = getAll()
                )
            }

            single {
                MessagingConfig(
                    config = get(),
                    producers = getAll(),
                    consumers = getAll()
                )
            } withOptions {
                bind<InitializingComponent>()
            }

            singleOf(HealthConfig::configure) {
                bind<Service>()
            }

            singleOf(::FlywayConfig) {
                bind<InitializingComponent>()
            }

            singleOf(::UsersRepository)

            singleOf(::UsersApi) {
                bind<Service>()
            }

            singleOf(::UserEventsProducer) {
                bind<EventProducer<UserCreated>>()
            }

            singleOf(::UsersProjector) {
                bind<EventConsumer<UserCreated>>()
            }
        }

        return startKoin {
            modules(appModule)
        }
    }
}