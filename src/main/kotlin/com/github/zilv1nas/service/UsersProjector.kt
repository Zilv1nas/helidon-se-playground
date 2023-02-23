package com.github.zilv1nas.service

import com.github.zilv1nas.repository.UsersRepository
import com.github.zilv1nas.service.model.UserCreated
import io.helidon.messaging.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UsersProjector(private val usersRepository: UsersRepository) : EventConsumer<UserCreated>() {
    override val logger: Logger = LoggerFactory.getLogger(UsersProjector::class.java)

    override val channel: Channel<UserCreated> = Channel.create("users")

    override fun on(event: UserCreated) {
        usersRepository.save(event.toUser()).get()
    }
}