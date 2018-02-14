package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.exception.InvalidSecretException
import com.github.matek2305.izardbets.exception.ValidationFailedException
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.springframework.http.HttpStatus

object RestExceptionHandlerSpec : Spek({

    describe("rest exception handler") {

        val exceptionHandler = RestExceptionHandler()

        it("should map invalid secret to FORBIDDEN") {

            val exception = InvalidSecretException("message")
            val errorMessage = exceptionHandler.handleInvalidSecretException(exception)

            assertThat(errorMessage.message, equalTo(exception.message))
            assertThat(errorMessage.status, equalTo(HttpStatus.FORBIDDEN))
        }

        it("should map failed validation as BAD_REQUEST") {

            val exception = ValidationFailedException("message")
            val errorMessage = exceptionHandler.handleValidationFailedException(exception)

            assertThat(errorMessage.message, equalTo(exception.message))
            assertThat(errorMessage.status, equalTo(HttpStatus.BAD_REQUEST))
        }
    }
})
