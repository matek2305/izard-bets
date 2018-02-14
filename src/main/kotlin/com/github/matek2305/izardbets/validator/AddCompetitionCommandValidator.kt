package com.github.matek2305.izardbets.validator

import com.github.matek2305.izardbets.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.exception.ValidationFailedException
import org.springframework.stereotype.Component

@Component
class AddCompetitionCommandValidator {

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
