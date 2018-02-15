package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.common.RestExceptionHandler
import com.github.matek2305.izardbets.common.exception.InvalidSecretException
import com.github.matek2305.izardbets.common.exception.ValidationFailedException
import com.github.matek2305.izardbets.competitions.CompetitionController
import com.github.matek2305.izardbets.competitions.CompetitionService
import com.github.matek2305.izardbets.competitions.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.competitions.api.CreateEventCommand
import com.github.matek2305.izardbets.competitions.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.competitions.domain.Competition
import com.github.matek2305.izardbets.competitions.domain.Event
import com.github.matek2305.izardbets.competitions.validator.CreateCompetitionCommandValidator
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isEmpty
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

object CompetitionControllerSpec : Spek({

    describe("competitions controller") {

        val competitionServiceMock = mock(CompetitionService::class.java)
        val addCompetitionCommandValidatorMock = mock(CreateCompetitionCommandValidator::class.java)

        val webTestClient = WebTestClient
            .bindToController(CompetitionController(
                competitionServiceMock,
                addCompetitionCommandValidatorMock))
            .controllerAdvice(RestExceptionHandler())
            .build()

        it("should return all competitions") {
            val competition1 = Competition(
                id = "1",
                name = "name",
                description = "desc",
                secret = "secret",
                invitationCode = "asd687ds",
                type = Competition.Type.SINGLE_EVENT)

            val competition2 = Competition(
                id = "2",
                name = "name",
                description = "desc",
                secret = "secret",
                invitationCode = "asd4s45s",
                type = Competition.Type.SINGLE_EVENT)

            given(competitionServiceMock.findAll())
                .willReturn(Flux.just(competition1, competition2))

            webTestClient.get().uri("/competitions").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$").isArray
                .jsonPath("$", hasSize(equalTo(2)))
        }

        it("should return single competitions") {
            val competition = Competition(
                id = "1",
                name = "name",
                description = "desc",
                secret = "secret",
                invitationCode = "as2387ds",
                type = Competition.Type.SINGLE_EVENT)

            given(competitionServiceMock.findById(competition.id!!))
                .willReturn(Mono.just(competition))

            webTestClient.get().uri("/competitions/${competition.id}")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(competition.id!!)
                .jsonPath("$.name").isEqualTo(competition.name)
                .jsonPath("$.secret").doesNotExist()
                .jsonPath("$.invitationCode").isEqualTo(competition.invitationCode)
                .jsonPath("$.type").isEqualTo(competition.type.name)
                .jsonPath("$.events", isEmpty).isArray
        }

        it("should return not found for not existing competitions") {
            val competitionId = "1"

            given(competitionServiceMock.findById(competitionId))
                .willReturn(Mono.empty())

            webTestClient.get().uri("/competitions/$competitionId")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound
        }

        it("should return bad request for invalid add competitions command") {
            val addCompetitionCommand = CreateCompetitionCommand(
                name = "Barcelona vs Chelsea",
                type = Competition.Type.SINGLE_EVENT,
                secret = "secret")

            val validationErrorMessage = "message"
            given(addCompetitionCommandValidatorMock.validate(addCompetitionCommand))
                .willThrow(ValidationFailedException(validationErrorMessage))

            webTestClient.post().uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(addCompetitionCommand), CreateCompetitionCommand::class.java)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name)
                .jsonPath("$.message").isEqualTo(validationErrorMessage)
        }

        it("should create competitions") {
            val addCompetitionCommand = CreateCompetitionCommand(
                name = "Barcelona vs Chelsea",
                type = Competition.Type.SINGLE_EVENT,
                secret = "secret",
                events = listOf(
                    CreateEventCommand(
                        homeTeamName = "Barcelona",
                        awayTeamName = "Chelsea",
                        date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45))))

            val competition = Competition(
                id = "1",
                name = "Barcelona vs Chelsea",
                secret = "secret",
                invitationCode = "asuda7y3",
                type = Competition.Type.SINGLE_EVENT,
                events = listOf(
                    Event(
                        id = "1",
                        homeTeamName = "Barcelona",
                        awayTeamName = "Chelsea",
                        date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45))))

            given(addCompetitionCommandValidatorMock.validate(addCompetitionCommand))
                .willReturn(addCompetitionCommand)

            given(competitionServiceMock.create(addCompetitionCommand))
                .willReturn(Mono.just(competition))

            webTestClient.post().uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(addCompetitionCommand), CreateCompetitionCommand::class.java)
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.id").isEqualTo(competition.id!!)
                .jsonPath("$.name").isEqualTo(competition.name)
                .jsonPath("$.secret").doesNotExist()
                .jsonPath("$.invitationCode").isEqualTo(competition.invitationCode)
                .jsonPath("$.type").isEqualTo(competition.type.name)
                .jsonPath("$.events", hasSize(equalTo(1))).isArray
                .jsonPath("$.events[0].id").isEqualTo(competition.events[0].id)
                .jsonPath("$.events[0].homeTeamName").isEqualTo(competition.events[0].homeTeamName)
                .jsonPath("$.events[0].awayTeamName").isEqualTo(competition.events[0].awayTeamName)
//                .jsonPath("$.events[0].date").isEqualTo(competitions.events[0].date)
        }

        it("should update event score") {
            val competitionId = "1"
            val eventId = "1"
            val command = UpdateEventScoreCommand(
                competitionSecret = "secret",
                homeTeamScore = 1,
                awayTeamScore = 1)

            given(competitionServiceMock.updateEvent(competitionId, eventId, command))
                .willReturn(Mono.just(mock(Competition::class.java)))

            webTestClient.patch().uri("/competitions/$competitionId/events/$eventId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(command), UpdateEventScoreCommand::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody().isEmpty
        }

        it("should return forbidden for invalid secret") {
            val competitionId = "1"
            val eventId = "1"
            val errorMessage = "message"
            val command = UpdateEventScoreCommand(
                competitionSecret = "secret",
                homeTeamScore = 1,
                awayTeamScore = 1)

            given(competitionServiceMock.updateEvent(competitionId, eventId, command))
                .willReturn(Mono.error(InvalidSecretException(errorMessage)))

            webTestClient.patch().uri("/competitions/$competitionId/events/$eventId")
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
