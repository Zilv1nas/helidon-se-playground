package com.github.zilv1nas.service

import com.github.zilv1nas.service.model.Event
import io.helidon.messaging.Emitter

interface EventProducer<E : Event> {
    val emitter: Emitter<E>
}