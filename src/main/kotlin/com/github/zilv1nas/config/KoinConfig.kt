package com.github.zilv1nas.config

import com.github.zilv1nas.config.serialization.ObjectMapperFactory
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
                    .build()
            }

            single { FlywayConfig(config = get()) } withOptions {
                bind<InitializingComponent>()
            }

            single { MessagingConfig(config = get(), producers = getAll(), consumers = getAll()) } withOptions {
                bind<InitializingComponent>()
            }

            single {
                AppWebServer(
                    config = get(),
                    objectMapper = get(),
                    apis = getAll(),
                    initializingComponents = getAll()
                )
            }

            single { UsersRepository(dbClient = get()) }

            single { UsersApi(usersRepository = get(), userEventsProducer = get()) } withOptions {
                bind<Service>()
            }

            single { UserEventsProducer() } withOptions {
                bind<EventProducer<UserCreated>>()
            }

            single { UsersProjector(usersRepository = get()) } withOptions {
                bind<EventConsumer<UserCreated>>()
            }
        }

        return startKoin {
            modules(appModule)
        }
    }
}