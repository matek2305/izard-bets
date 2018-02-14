package com.github.matek2305.izardbets.validator

import com.github.matek2305.izardbets.api.CreateCompetitionCommand
import com.github.matek2305.izardbets.api.CreateEventCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.exception.ValidationFailedException
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.mockito.Mockito.mock

class AddCompetitionCommandValidatorSpec : Spek({

    describe("add competition command validator") {

        val validator = AddCompetitionCommandValidator()

        it("should fail for SINGLE_EVENT competition type with empty event list") {

            assertThat({
                validator.validate(singleEventCompetitionWith(emptyList()))
            }, throws<ValidationFailedException>())
        }

        it("should fail for SINGLE_EVENT competition type with more than one event") {

            val event1 = mock(CreateEventCommand::class.java)
            val event2 = mock(CreateEventCommand::class.java)

            assertThat({
                validator.validate(singleEventCompetitionWith(listOf(event1, event2)))
            }, throws<ValidationFailedException>())
        }

        it("should pass for SINGLE_EVENT competition type with exactly one event") {

            val event = mock(CreateEventCommand::class.java)
            val command = singleEventCompetitionWith(listOf(event))

            assertThat(validator.validate(command), equalTo(command))
        }
    }
})

private fun singleEventCompetitionWith(events: List<CreateEventCommand>): CreateCompetitionCommand {
    return CreateCompetitionCommand(
        name = "sample",
        type = Competition.Type.SINGLE_EVENT,
        secret = "secret",
        events = events)
}
