package com.github.matek2305.izardbets.competitions

import com.github.matek2305.izardbets.competitions.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.competitions.api.CreateEventCommand
import com.github.matek2305.izardbets.competitions.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.competitions.domain.Competition
import com.github.matek2305.izardbets.competitions.validator.CreateCompetitionCommandValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class CompetitionController(
    private val competitionService: CompetitionService,
    private val createCompetitionCommandValidator: CreateCompetitionCommandValidator
) {

    @GetMapping("/competitions")
    fun findAll(): Flux<Competition> = competitionService.findAll()

    @GetMapping("/competitions/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<Competition>> =
        competitionService
            .findById(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))

    @PostMapping("/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody command: Mono<CreateCompetitionCommand>): Mono<Competition> =
        command
            .map { createCompetitionCommandValidator.validate(it) }
            .flatMap { competitionService.create(it) }

    @PostMapping("/competitions/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    fun addEvent(@PathVariable id: String, @RequestBody command: Mono<CreateEventCommand>): Mono<Competition> =
        command.flatMap { competitionService.addEvent(id, it) }

    @PatchMapping("/competitions/{competitionId}/events/{eventId}")
    fun updateEvent(
        @PathVariable competitionId: String,
        @PathVariable eventId: String,
        @RequestBody command: Mono<UpdateEventScoreCommand>): Mono<ResponseEntity<Void>> =

        command
            .flatMap { competitionService.updateEvent(competitionId, eventId, it) }
            .map { ResponseEntity<Void>(HttpStatus.OK) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
}
