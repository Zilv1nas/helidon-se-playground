package com.github.zilv1nas.web.model

import com.github.zilv1nas.repository.model.User
import java.util.UUID

data class UserRequest(val email: String) {
    fun toUser(id: UUID) = User(id, email)
}
