package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.domain.Event
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
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
            val competition1 = Competition(id = "1", name = "name", description = "desc", type = Competition.Type.SINGLE_EVENT)
            val competition2 = Competition(id = "2", name = "name", description = "desc", type = Competition.Type.SINGLE_EVENT)

            given(competitionServiceMock.findAll())
                .willReturn(Flux.just(competition1, competition2))

            webTestClient.get().uri("/competitions").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Competition::class.java)
                .hasSize(2)
                .contains(competition1, competition2)
        }

        it("should return single competition") {
            val competition = Competition(id = "1", name = "name", description = "desc", type = Competition.Type.SINGLE_EVENT)

            given(competitionServiceMock.findById(competition.id!!))
                .willReturn(Mono.just(competition))

            webTestClient.get().uri("/competitions/${competition.id}").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectBody(Competition::class.java)
                .isEqualTo<Nothing?>(competition)
        }

        it("should create competition") {
            val addCompetitionCommand = AddCompetitionCommand(
                name = "Barcelona vs Chelsea",
                type = Competition.Type.SINGLE_EVENT,
                events = listOf(
                    AddEventCommand(
                        homeTeamName = "Barcelona",
                        awayTeamName = "Chelsea",
                        date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45))))

            val competition = Competition(
                id = "1",
                name = "Barcelona vs Chelsea",
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
        }
    }
})
