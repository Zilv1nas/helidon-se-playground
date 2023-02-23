package com.github.zilv1nas.web.model

import com.github.zilv1nas.repository.model.User
import java.util.UUID

data class UserResponse(val id: UUID, val email: String) {
    companion object {
        fun from(user: User) = UserResponse(user.id, user.email)
    }
}
