package com.github.zilv1nas.service

import com.github.zilv1nas.repository.UsersRepository
import com.github.zilv1nas.service.model.UserCreated
import org.slf4j.LoggerFactory

class UsersProjector(private val usersRepository: UsersRepository) : EventConsumer<UserCreated>(
    channelName = "users",
    logger = LoggerFactory.getLogger(UsersProjector::class.java)
) {
    override fun on(event: UserCreated) {
        usersRepository.save(event.toUser()).get()
    }
}