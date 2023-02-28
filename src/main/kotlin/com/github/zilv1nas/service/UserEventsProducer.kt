package com.github.zilv1nas.service

import com.github.zilv1nas.service.model.UserCreated
import org.slf4j.LoggerFactory


class UserEventsProducer : EventProducer<UserCreated>(
    channelName = "users-out",
    logger = LoggerFactory.getLogger(UserEventsProducer::class.java),
)