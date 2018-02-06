package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.domain.Competition
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
class CompetitionControllerSpec extends Specification {

    def competitionServiceMock = Mock(CompetitionService)
    def webTestClient = WebTestClient
        .bindToController(new CompetitionController(competitionServiceMock))
        .build()

    def "should return all competitions"() {
        given:
            def competition1 = new Competition('1', 'name', 'desc', [], null, null)
            def competition2 = new Competition('2', 'name', 'desc', [], null, null)
            competitionServiceMock.findAll() >> Flux.just(competition1, competition2)
        expect:
            webTestClient.get().uri('/competitions').accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Competition)
                .hasSize(2)
                .contains(competition1, competition2)
    }

    def "should return single competition"() {
        given:
            def competition = new Competition('1', 'name', 'desc', [], null, null)
            competitionServiceMock.findById('1') >> Mono.just(competition)
        expect:
            webTestClient.get().uri("/competitions/$competition.id").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Competition)
                .isEqualTo(competition)
    }
}
