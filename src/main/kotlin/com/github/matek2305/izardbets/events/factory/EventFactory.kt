package com.github.matek2305.izardbets.events.factory

import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.events.api.CreateEventCommand
import com.github.matek2305.izardbets.events.domain.Event
import org.springframework.stereotype.Component

@Component
class EventFactory(
    private val invitationCodeGenerator: InvitationCodeGenerator,
    private val secretEncoder: SecretEncoder
) {
    fun build(command: CreateEventCommand): Event = Event(
        homeTeamName = command.homeTeamName,
        awayTeamName = command.awayTeamName,
        date = command.date,
        description = command.description,
        secret = secretEncoder.encode(command.secret),
        invitationCode = invitationCodeGenerator.generate()
    )
}
