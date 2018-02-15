package com.github.matek2305.izardbets.competition.factory

import com.github.matek2305.izardbets.competition.api.CreateEventCommand
import com.github.matek2305.izardbets.competition.domain.Event
import org.springframework.stereotype.Component

@Component
class EventFactory(private val eventIdGenerator: EventIdGenerator) {

    fun build(command: CreateEventCommand): Event = Event(
        id = eventIdGenerator.generate(),
        homeTeamName = command.homeTeamName,
        awayTeamName = command.awayTeamName,
        date = command.date)
}
