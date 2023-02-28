package com.github.zilv1nas.service.model

class EventEnvelope<E : Event> private constructor(
    val data: E,
    val metadata: Map<String, Any>,
) {
    companion object {
        fun <E : Event> create(event: E, metadata: Map<String, Any> = emptyMap()) = EventEnvelope(
            data = event,
            metadata = metadata
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventEnvelope<*>

        if (data != other.data) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }
}