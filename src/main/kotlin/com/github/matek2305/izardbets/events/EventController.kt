package com.github.matek2305.izardbets.events

import com.github.matek2305.izardbets.events.api.CreateEventCommand
import com.github.matek2305.izardbets.events.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.events.domain.Event
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class EventController(private val eventService: EventService) {

    @GetMapping("/events")
    fun findAll(): Flux<Event> = eventService.findAll()

    @GetMapping("/events/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<Event>> =
        eventService
            .findById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody command: Mono<CreateEventCommand>): Mono<Event> =
        command.flatMap { eventService.create(it) }

    @PutMapping("/events/{id}")
    fun updateEvent(
        @PathVariable id: String,
        @RequestBody command: Mono<UpdateEventScoreCommand>
    ): Mono<ResponseEntity<Void>> {
        return command
            .flatMap { eventService.updateScore(id, it) }
            .map { ResponseEntity<Void>(HttpStatus.OK) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }
}
