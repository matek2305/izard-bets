package com.github.matek2305.izardbets.events

import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.common.exception.InvalidSecretException
import com.github.matek2305.izardbets.events.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.events.domain.Event
import com.github.matek2305.izardbets.events.domain.EventRepository
import com.github.matek2305.izardbets.events.factory.EventFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class EventServiceSpec : Spek({

    describe("event service") {

        val eventRepositoryMock = mock(EventRepository::class.java)
        val eventFactoryMock = mock(EventFactory::class.java)
        val secretEncoderMock = mock(SecretEncoder::class.java)

        val eventService = EventService(
            eventRepositoryMock,
            eventFactoryMock,
            secretEncoderMock
        )

        it("should throw invalid secret exception when updating event with invalid secret") {

            val eventId = "1"
            val eventMock = mock(Event::class.java)

            given(eventRepositoryMock.findById(eventId))
                .willReturn(Mono.just(eventMock))

            val command = UpdateEventScoreCommand(
                secret = "invalidSecret",
                homeTeamScore = 1,
                awayTeamScore = 1
            )

            given(secretEncoderMock.check(command.secret, eventMock.secret))
                .willReturn(false)

            StepVerifier
                .create(eventService.updateScore(eventId, command))
                .expectError(InvalidSecretException::class.java)
                .verify()
        }

        it("should update event score") {

            val eventId = "1"
            val eventMock = mock(Event::class.java)

            given(eventRepositoryMock.findById(eventId))
                .willReturn(Mono.just(eventMock))

            val command = UpdateEventScoreCommand(
                secret = "invalidSecret",
                homeTeamScore = 1,
                awayTeamScore = 1
            )

            given(secretEncoderMock.check(command.secret, eventMock.secret))
                .willReturn(true)

            val updatedCompetition = mock(Event::class.java)

            given(eventMock.updateScore(command.homeTeamScore, command.awayTeamScore))
                .willReturn(updatedCompetition)

            given(eventRepositoryMock.save(updatedCompetition))
                .willReturn(Mono.just(updatedCompetition))

            StepVerifier
                .create(eventService.updateScore(eventId, command))
                .expectNext(updatedCompetition)
                .verifyComplete()
        }
    }
})
