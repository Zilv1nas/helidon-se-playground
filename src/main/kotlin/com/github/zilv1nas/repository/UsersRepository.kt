package com.github.zilv1nas.repository

import com.github.zilv1nas.repository.model.User
import io.helidon.common.reactive.Multi
import io.helidon.common.reactive.Single
import io.helidon.dbclient.DbClient

class UsersRepository(private val dbClient: DbClient) {
    fun save(user: User): Single<Long> {
        val result = dbClient.execute {
            it.createInsert("INSERT INTO users (id, email) VALUES (:id, :email) ON CONFLICT DO NOTHING")
                .params(user.asParams())
                .execute()
        }

        return result
    }

    fun findAll(): Multi<User> {
        val result = dbClient.execute {
            it.createQuery("SELECT * FROM users").execute()
        }
        return result.map { User.from(it) }
    }
}