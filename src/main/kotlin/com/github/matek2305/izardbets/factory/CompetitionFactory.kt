package com.github.matek2305.izardbets.factory

import com.github.matek2305.izardbets.InvitationCodeGenerator
import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.domain.Competition
import org.springframework.stereotype.Component

@Component
class CompetitionFactory(
    private val invitationCodeGenerator: InvitationCodeGenerator,
    private val eventFactory: EventFactory
) {
    fun build(command: AddCompetitionCommand): Competition = Competition(
        name = command.name,
        description = command.description,
        type = command.type,
        invitationCode = invitationCodeGenerator.generate(),
        events = command.events.map { eventFactory.build(it) })
}
