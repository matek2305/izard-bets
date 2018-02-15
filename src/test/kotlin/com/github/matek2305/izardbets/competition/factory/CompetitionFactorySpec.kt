package com.github.matek2305.izardbets.competition.factory

import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.competition.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.competition.api.CreateEventCommand
import com.github.matek2305.izardbets.competition.domain.Competition
import com.github.matek2305.izardbets.competition.domain.Event
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class CompetitionFactorySpec : Spek({

    describe("competition factory") {

        val secretEncoderMock = mock(SecretEncoder::class.java)
        val invitationCodeGeneratorMock = mock(InvitationCodeGenerator::class.java)
        val eventFactoryMock = mock(EventFactory::class.java)
        val competitionFactory = CompetitionFactory(secretEncoderMock, invitationCodeGeneratorMock, eventFactoryMock)

        it("should build competition domain model with generated invitation code") {
            val eventCommandMock = mock(CreateEventCommand::class.java)
            val command = CreateCompetitionCommand(
                name = "Barcelona vs Chelsea",
                description = "some description",
                type = Competition.Type.SINGLE_EVENT,
                secret = "secret",
                events = listOf(eventCommandMock))

            val encodedSecret = "encoded"
            given(secretEncoderMock.encode(command.secret)).willReturn(encodedSecret)

            val generatedInvCode = "787ads8s"
            given(invitationCodeGeneratorMock.generate()).willReturn(generatedInvCode)

            val eventMock = mock(Event::class.java)
            given(eventFactoryMock.build(eventCommandMock)).willReturn(eventMock)

            val competition = competitionFactory.build(command)
            assertThat(competition.name, equalTo(command.name))
            assertThat(competition.description, equalTo(command.description))
            assertThat(competition.type, equalTo(command.type))
            assertThat(competition.secret, equalTo(encodedSecret))
            assertThat(competition.invitationCode, equalTo(generatedInvCode))
            assertThat(competition.events, hasSize(equalTo(1)) and hasElement(eventMock))
        }
    }
})
