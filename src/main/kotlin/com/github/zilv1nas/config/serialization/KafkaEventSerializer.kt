package com.github.zilv1nas.config.serialization

import com.github.zilv1nas.service.model.EventEnvelope
import com.github.zilv1nas.service.model.UserCreated
import org.apache.kafka.common.serialization.Serializer

// TODO implement generic serializer for all event types
class KafkaEventSerializer : Serializer<EventEnvelope<UserCreated>> {
    private val objectMapper = ObjectMapperFactory.create()

    override fun serialize(topic: String, data: EventEnvelope<UserCreated>): ByteArray =
        objectMapper.writeValueAsBytes(data)
}