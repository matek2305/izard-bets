package com.github.matek2305.izardbets.factory

import com.github.matek2305.izardbets.EventIdGenerator
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.domain.Event
import org.springframework.stereotype.Component

@Component
class EventFactory(private val eventIdGenerator: EventIdGenerator) {

    fun build(command: AddEventCommand): Event = Event(
        id = eventIdGenerator.generate(),
        homeTeamName = command.homeTeamName,
        awayTeamName = command.awayTeamName,
        date = command.date)
}
