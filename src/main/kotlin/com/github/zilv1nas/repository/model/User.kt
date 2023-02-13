package com.github.zilv1nas.repository.model

import io.helidon.dbclient.DbRow
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
) {
    companion object {
        fun from(row: DbRow) = User(
            row.column("id").`as`(UUID::class.java),
            row.column("email").`as`(String::class.java),
        )
    }

    fun asParams() = mapOf(
        "id" to id,
        "email" to email,
    )
}
