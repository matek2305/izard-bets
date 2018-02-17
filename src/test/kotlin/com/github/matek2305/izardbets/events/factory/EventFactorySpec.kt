package com.github.matek2305.izardbets.events.factory

import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.events.api.CreateEventCommand
import com.github.matek2305.izardbets.events.domain.Event
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

    describe("events factory") {

        val secretEncoderMock = mock(SecretEncoder::class.java)
        val invitationCodeGeneratorMock = mock(InvitationCodeGenerator::class.java)
        val eventFactory = EventFactory(invitationCodeGeneratorMock, secretEncoderMock)

        it("should build event domain model with generated invitation code") {
            val command = CreateEventCommand(
                homeTeamName = "Chelsea",
                awayTeamName = "Barcelona",
                description = "Champions League 2017/18 kick off stage",
                date = LocalDateTime.of(2018, Month.FEBRUARY, 20, 20, 45),
                secret = "secret"
            )

            val encodedSecret = "encoded"
            given(secretEncoderMock.encode(command.secret)).willReturn(encodedSecret)

            val generatedInvCode = "787ads8s"
            given(invitationCodeGeneratorMock.generate()).willReturn(generatedInvCode)

            val event = eventFactory.build(command)
            assertThat(event.homeTeamName, equalTo(command.homeTeamName))
            assertThat(event.awayTeamName, equalTo(command.awayTeamName))
            assertThat(event.description, equalTo(command.description))
            assertThat(event.date, equalTo(command.date))
            assertThat(event.secret, equalTo(encodedSecret))
            assertThat(event.invitationCode, equalTo(generatedInvCode))
            assertThat(event.status, equalTo(Event.Status.CREATED))
        }
    }
})
