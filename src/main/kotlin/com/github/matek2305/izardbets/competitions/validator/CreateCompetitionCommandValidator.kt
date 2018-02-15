package com.github.matek2305.izardbets.competitions.validator

import com.github.matek2305.izardbets.common.exception.ValidationFailedException
import com.github.matek2305.izardbets.competitions.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.competitions.domain.Competition
import org.springframework.stereotype.Component

@Component
class CreateCompetitionCommandValidator {

    fun validate(command: CreateCompetitionCommand): CreateCompetitionCommand {
        if (command.type == Competition.Type.SINGLE_EVENT) {
            validateSingleEventCompetition(command)
        }

        return command
    }

    private fun validateSingleEventCompetition(command: CreateCompetitionCommand) {
        if (command.events.size != 1) {
            throw ValidationFailedException("Competition with type=SINGLE_EVENT must have exactly one event!")
        }
    }
}
