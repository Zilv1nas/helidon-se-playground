package com.github.zilv1nas.service.model

import com.github.zilv1nas.repository.model.User
import java.util.UUID

data class UserCreated(val id: UUID, val email: String) : Event {
    fun toUser() = User(id, email)
}