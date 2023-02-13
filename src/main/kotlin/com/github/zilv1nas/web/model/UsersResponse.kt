package com.github.zilv1nas.web.model

import com.github.zilv1nas.repository.model.User

data class UsersResponse(val users: List<UserResponse>) {
    companion object {
        fun from(users: Iterable<User>) = UsersResponse(users.map { UserResponse.from(it) })
    }
}
