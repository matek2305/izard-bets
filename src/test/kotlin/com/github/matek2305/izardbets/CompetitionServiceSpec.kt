package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.exception.InvalidSecretException
import com.github.matek2305.izardbets.factory.CompetitionFactory
import com.github.matek2305.izardbets.factory.EventFactory
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import reactor.core.publisher.Mono

class CompetitionServiceSpec : Spek({

    describe("competition service") {

        val competitionRepositoryMock = mock(CompetitionRepository::class.java)
        val competitionFactoryMock = mock(CompetitionFactory::class.java)
        val eventFactoryMock = mock(EventFactory::class.java)
        val secretEncoderMock = mock(SecretEncoder::class.java)

        val competitionService = CompetitionService(
            competitionRepositoryMock,
            competitionFactoryMock,
            eventFactoryMock,
            secretEncoderMock)

        it("should throw invalid secret exception when updating event with invalid secret") {

            val competitionId = "competitionId"
            val competitionMock = mock(Competition::class.java)

            given(competitionRepositoryMock.findById(competitionId))
                .willReturn(Mono.just(competitionMock))

            val command = UpdateEventScoreCommand(
                competitionSecret = "invalidSecret",
                homeTeamScore = 1,
                awayTeamScore = 1)

            given(secretEncoderMock.check(command.competitionSecret, competitionMock.secret))
                .willReturn(false)

            assertThat({
                competitionService.updateEvent(competitionId, "eventId", command).block()
            }, throws<InvalidSecretException>())
        }
    }
})
