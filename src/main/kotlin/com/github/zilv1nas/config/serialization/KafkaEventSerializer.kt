package com.github.zilv1nas.config.serialization

import com.github.zilv1nas.service.model.UserCreated
import org.apache.kafka.common.serialization.Serializer

// TODO implement generic serializer for all event types
class KafkaEventSerializer : Serializer<UserCreated> {
    private val objectMapper = ObjectMapperFactory.create()

    override fun serialize(topic: String, data: UserCreated): ByteArray = objectMapper.writeValueAsBytes(data)
}