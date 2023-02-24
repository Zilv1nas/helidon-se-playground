package com.github.zilv1nas.service

import com.github.zilv1nas.service.model.UserCreated
import io.helidon.common.reactive.Single
import io.helidon.messaging.Channel
import io.helidon.messaging.Emitter
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class UserEventsProducer : EventProducer<UserCreated> {
    private val logger: Logger = LoggerFactory.getLogger(UserEventsProducer::class.java)

    override val emitter: Emitter<UserCreated> = Emitter.create(Channel.create("users-out"))

    fun publish(event: UserCreated): Single<Void> {
        logger.info("Publishing event: $event")
        return Single.create(emitter.send(event), true)
    }
}