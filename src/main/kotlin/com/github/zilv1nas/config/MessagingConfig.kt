package com.github.zilv1nas.config

import com.github.zilv1nas.service.EventConsumer
import com.github.zilv1nas.service.EventProducer
import com.github.zilv1nas.service.model.Event
import com.github.zilv1nas.service.model.EventEnvelope
import io.helidon.config.Config
import io.helidon.messaging.Messaging
import io.helidon.messaging.connectors.kafka.KafkaConnector

class MessagingConfig(
    private val config: Config,
    private val producers: List<EventProducer<Event>>,
    private val consumers: List<EventConsumer<Event>>,
) : InitializingComponent {
    private lateinit var messaging: Messaging

    override fun start() {
        messaging = Messaging.builder()
            .config(config)
            .apply { producers.forEach { emitter(it.emitter) } }
            .apply { consumers.forEach { subscriber(it.channel, it.listener) } }
            .connector(KafkaConnector.create())
            .build()
            .start()
    }

    override fun stop() {
        messaging.stop()
    }
}