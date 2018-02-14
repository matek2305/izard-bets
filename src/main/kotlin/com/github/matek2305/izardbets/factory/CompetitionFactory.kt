package com.github.matek2305.izardbets.factory

import com.github.matek2305.izardbets.InvitationCodeGenerator
import com.github.matek2305.izardbets.SecretEncoder
import com.github.matek2305.izardbets.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.domain.Competition
import org.springframework.stereotype.Component

@Component
class CompetitionFactory(
    private val secretEncoder: SecretEncoder,
    private val invitationCodeGenerator: InvitationCodeGenerator,
    private val eventFactory: EventFactory
) {
    fun build(command: CreateCompetitionCommand): Competition = Competition(
        name = command.name,
        description = command.description,
        type = command.type,
        secret = secretEncoder.encode(command.secret),
        invitationCode = invitationCodeGenerator.generate(),
        events = command.events.map { eventFactory.build(it) })
}
