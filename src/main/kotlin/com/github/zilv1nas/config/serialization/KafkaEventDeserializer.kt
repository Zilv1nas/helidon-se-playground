package com.github.zilv1nas.config.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import com.github.zilv1nas.service.model.EventEnvelope
import com.github.zilv1nas.service.model.UserCreated
import org.apache.kafka.common.serialization.Deserializer

// TODO implement generic deserializer for all event types
class KafkaEventDeserializer : Deserializer<EventEnvelope<UserCreated>> {
    private val objectMapper = ObjectMapperFactory.create()

    override fun deserialize(topic: String, data: ByteArray): EventEnvelope<UserCreated> = objectMapper.readValue(data)
}