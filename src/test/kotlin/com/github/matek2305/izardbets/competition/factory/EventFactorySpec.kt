package com.github.matek2305.izardbets.competition.factory

import com.github.matek2305.izardbets.competition.api.CreateEventCommand
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.time.Month

class EventFactorySpec : Spek({

    describe("event factory") {

        val eventIdGeneratorMock = mock(EventIdGenerator::class.java)
        val eventFactory = EventFactory(eventIdGeneratorMock)

        it("should build event domain model with generated id") {
            val command = CreateEventCommand(
                homeTeamName = "Barcelona",
                awayTeamName = "Chelsea",
                date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45))

            val generatedId = "123"
            given(eventIdGeneratorMock.generate()).willReturn(generatedId)

            val event = eventFactory.build(command)

            assertThat(event.id, equalTo(generatedId))
            assertThat(event.homeTeamName, equalTo(command.homeTeamName))
            assertThat(event.awayTeamName, equalTo(command.awayTeamName))
            assertThat(event.date, equalTo(command.date))
            assertThat(event.homeTeamScore, absent())
            assertThat(event.awayTeamScore, absent())
        }
    }
})
