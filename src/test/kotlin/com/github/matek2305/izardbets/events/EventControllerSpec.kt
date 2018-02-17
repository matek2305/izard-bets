package com.github.matek2305.izardbets.events

import com.github.matek2305.izardbets.common.RestExceptionHandler
import com.github.matek2305.izardbets.common.exception.InvalidSecretException
import com.github.matek2305.izardbets.events.api.CreateEventCommand
import com.github.matek2305.izardbets.events.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.events.domain.Event
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.Month

object EventControllerSpec : Spek({

    describe("events controller") {

        val eventServiceMock = mock(EventService::class.java)

        val webTestClient = WebTestClient
            .bindToController(EventController(eventServiceMock))
            .controllerAdvice(RestExceptionHandler())
            .build()

        it("should return all events") {

            val chelseaVsBarcelona = Event(
                id = "1",
                homeTeamName = "Chelsea",
                awayTeamName = "Barcelona",
                date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45),
                secret = "secret",
                invitationCode = "asd687ds"
            )

            val tottenhamVsJuventus = Event(
                id = "2",
                homeTeamName = "Tottenham",
                awayTeamName = "Juventus",
                date = LocalDateTime.of(2018, Month.MARCH, 7, 20, 45),
                secret = "secret",
                invitationCode = "asd4s45s")

            given(eventServiceMock.findAll())
                .willReturn(Flux.just(chelseaVsBarcelona, tottenhamVsJuventus))

            webTestClient.get().uri("/events").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$").isArray
                .jsonPath("$", hasSize(equalTo(2)))
        }

        it("should return single event") {

            val juventusVsTottenham = Event(
                id = "1",
                homeTeamName = "Chelsea",
                awayTeamName = "Barcelona",
                description = "Champions League 2017/18 kick off stage",
                date = LocalDateTime.of(2018, Month.FEBRUARY, 13, 20, 45),
                homeTeamScore = 2,
                awayTeamScore = 2,
                secret = "secret",
                invitationCode = "asd687ds",
                status = Event.Status.FINISHED,
                createdAt = LocalDateTime.now(),
                lastUpdateAt= LocalDateTime.now(),
                version = "1"
            )

            given(eventServiceMock.findById(juventusVsTottenham.id!!))
                .willReturn(Mono.just(juventusVsTottenham))

            webTestClient.get().uri("/events/${juventusVsTottenham.id}")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(juventusVsTottenham.id!!)
                .jsonPath("$.homeTeamName").isEqualTo(juventusVsTottenham.homeTeamName)
                .jsonPath("$.awayTeamName").isEqualTo(juventusVsTottenham.awayTeamName)
                .jsonPath("$.description").isEqualTo(juventusVsTottenham.description!!)
                .jsonPath("$.homeTeamScore").isEqualTo(juventusVsTottenham.homeTeamScore!!)
                .jsonPath("$.awayTeamScore").isEqualTo(juventusVsTottenham.awayTeamScore!!)
                .jsonPath("$.secret").doesNotExist()
                .jsonPath("$.invitationCode").isEqualTo(juventusVsTottenham.invitationCode)
                .jsonPath("$.status").isEqualTo(juventusVsTottenham.status.name)
                .jsonPath("$.version").isEqualTo(juventusVsTottenham.version!!)
        }

        it("should return not found for not existing event") {

            val eventId = "1"

            given(eventServiceMock.findById(eventId))
                .willReturn(Mono.empty())

            webTestClient.get().uri("/events/$eventId")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound
        }

        it("should create event") {

            val command = CreateEventCommand(
                homeTeamName = "Chelsea",
                awayTeamName = "Barcelona",
                description = "Champions League 2017/18 kick off stage",
                date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45),
                secret = "secret"
            )

            val chelseaVsBarcelona = Event(
                id = "1",
                homeTeamName = "Chelsea",
                awayTeamName = "Barcelona",
                description = "Champions League 2017/18 kick off stage",
                date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45),
                secret = "secret",
                invitationCode = "asd687ds"
            )

            given(eventServiceMock.create(command))
                .willReturn(Mono.just(chelseaVsBarcelona))

            webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(command), CreateEventCommand::class.java)
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.id").isEqualTo(chelseaVsBarcelona.id!!)
                .jsonPath("$.homeTeamName").isEqualTo(chelseaVsBarcelona.homeTeamName)
                .jsonPath("$.awayTeamName").isEqualTo(chelseaVsBarcelona.awayTeamName)
                .jsonPath("$.description").isEqualTo(chelseaVsBarcelona.description!!)
                .jsonPath("$.secret").doesNotExist()
                .jsonPath("$.invitationCode").isEqualTo(chelseaVsBarcelona.invitationCode)
                .jsonPath("$.status").isEqualTo(Event.Status.CREATED.name)
        }

        it("should update event score") {

            val eventId = "1"
            val command = UpdateEventScoreCommand(
                secret = "secret",
                homeTeamScore = 1,
                awayTeamScore = 1
            )

            given(eventServiceMock.updateScore(eventId, command))
                .willReturn(Mono.just(mock(Event::class.java)))

            webTestClient.put().uri("/events/$eventId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(command), UpdateEventScoreCommand::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody().isEmpty
        }

        it("should return forbidden for invalid secret") {

            val eventId = "1"
            val errorMessage = "message"
            val command = UpdateEventScoreCommand(
                secret = "secret",
                homeTeamScore = 1,
                awayTeamScore = 1
            )

            given(eventServiceMock.updateScore(eventId, command))
                .willReturn(Mono.error(InvalidSecretException(errorMessage)))

            webTestClient.put().uri("/events/$eventId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(command), UpdateEventScoreCommand::class.java)
                .exchange()
                .expectStatus().isForbidden
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.FORBIDDEN.name)
                .jsonPath("$.message").isEqualTo(errorMessage)
        }
    }
})
