package com.github.zilv1nas.web

import com.github.zilv1nas.repository.UsersRepository
import com.github.zilv1nas.service.UserEventsProducer
import com.github.zilv1nas.web.model.UserRequest
import com.github.zilv1nas.web.model.UsersResponse
import io.helidon.webserver.Handler
import io.helidon.webserver.Routing
import io.helidon.webserver.ServerRequest
import io.helidon.webserver.ServerResponse
import io.helidon.webserver.Service
import java.util.UUID

class UsersApi(
    private val usersRepository: UsersRepository,
    private val userEventsProducer: UserEventsProducer,
) : Service {
    override fun update(rules: Routing.Rules) {
        with(rules) {
            post("/v1/users", handler(::saveUser))
            get("/v1/users", ::getUsers)
        }
    }

    private fun saveUser(request: ServerRequest, response: ServerResponse, userRequest: UserRequest) {
        val event = userRequest.toEvent(UUID.randomUUID())
        response.send(userEventsProducer.publish(event, request.spanContext().orElse(null)), Void::class.java)
    }

    private fun getUsers(request: ServerRequest, response: ServerResponse) {
        val users = usersRepository.findAll().collectList()
            .map { UsersResponse.from(it) }

        response.send(users, UsersResponse::class.java)
    }

    private inline fun <reified T> handler(entityHandler: Handler.EntityHandler<T>) =
        Handler.create(T::class.java, entityHandler)
}