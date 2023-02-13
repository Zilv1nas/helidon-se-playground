package com.github.zilv1nas.config

import com.github.zilv1nas.repository.UsersRepository
import com.github.zilv1nas.web.UsersApi
import io.helidon.config.Config
import io.helidon.dbclient.DbClient
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

object KoinConfig {
    fun configure(): KoinApplication {
        val appModule = module {
            single { Config.create() }

            single {
                DbClient.builder()
                    .config(get<Config>().get("db"))
                    .build()
            }

            single { FlywayConfig(config = get()) }

            single { AppWebServer(config = get(), usersApi = get()) }

            single { UsersRepository(dbClient = get()) }

            single { UsersApi(usersRepository = get()) }
        }

        return startKoin {
            modules(appModule)
        }
    }
}