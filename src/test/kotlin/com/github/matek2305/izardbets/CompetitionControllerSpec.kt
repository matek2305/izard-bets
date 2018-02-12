package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.domain.Event
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isEmpty
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.BDDMockito.verifyNoMoreInteractions
import org.mockito.Mockito.mock
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.Month

object CompetitionControllerSpec : Spek({

    describe("competition controller") {

        val competitionServiceMock = mock(CompetitionService::class.java)
        val webTestClient = WebTestClient
            .bindToController(CompetitionController(competitionServiceMock))
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

        it("should return single competition") {
            val competition = Competition(
                id = "1",
                name = "name",
                description = "desc",
                secret = "secret",
                invitationCode = "as2387ds",
                type = Competition.Type.SINGLE_EVENT)

            given(competitionServiceMock.findById(competition.id!!))
                .willReturn(Mono.just(competition))

            webTestClient.get().uri("/competitions/${competition.id}").accept(MediaType.APPLICATION_JSON_UTF8)
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

        it("should create competition") {
            val addCompetitionCommand = AddCompetitionCommand(
                name = "Barcelona vs Chelsea",
                type = Competition.Type.SINGLE_EVENT,
                secret = "secret",
                events = listOf(
                    AddEventCommand(
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

            given(competitionServiceMock.create(addCompetitionCommand))
                .willReturn(Mono.just(competition))

            webTestClient.post().uri("/competitions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(addCompetitionCommand))
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
                .jsonPath("$.events[0].date").isEqualTo(competition.events[0].date)
        }

        it("should update event score") {
            val competitionId = "competitionId"
            val eventId = "eventId"
            val command = UpdateEventScoreCommand(
                competitionSecret = "secret",
                homeTeamScore = 1,
                awayTeamScore = 1)

            webTestClient.patch().uri("/competitions/$competitionId/events/$eventId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(command))
                .exchange()
                .expectStatus().isOk
                .expectBody().isEmpty

            verify(competitionServiceMock).updateEvent(competitionId, eventId, command)
            verifyNoMoreInteractions(competitionServiceMock)
        }
    }
})
