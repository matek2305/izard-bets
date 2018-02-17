package com.github.matek2305.izardbets.events

import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.common.exception.InvalidSecretException
import com.github.matek2305.izardbets.events.api.CreateEventCommand
import com.github.matek2305.izardbets.events.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.events.domain.Event
import com.github.matek2305.izardbets.events.domain.EventRepository
import com.github.matek2305.izardbets.events.factory.EventFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Function
import java.util.function.Predicate

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventFactory: EventFactory,
    private val secretEncoder: SecretEncoder
) {
    fun findAll(): Flux<Event> = eventRepository.findAll()

    fun findById(id: String): Mono<Event> = eventRepository.findById(id)

    fun create(command: CreateEventCommand): Mono<Event> = eventRepository.save(eventFactory.build(command))

    fun updateScore(id: String, command: UpdateEventScoreCommand): Mono<Event> {
        return eventRepository.findById(id)
            .filter(validateSecret(command.secret))
            .map(updateEventScore(command))
            .flatMap { eventRepository.save(it) }
    }

    private fun validateSecret(secret: String) = Predicate { event: Event ->
        secretEncoder.check(secret, event.secret)
            || throw InvalidSecretException("Provided secret is not valid, update forbidden!")
    }

    private fun updateEventScore(command: UpdateEventScoreCommand) =
        Function { event: Event -> event.updateScore(command.homeTeamScore, command.awayTeamScore) }
}
