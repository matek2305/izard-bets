package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.exception.InvalidSecretException
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.springframework.http.HttpStatus

object RestExceptionHandlerSpec : Spek({

    given("rest exception handler") {

        val exceptionHandler = RestExceptionHandler()

        on("invalid secret exception occurs") {

            val exception = InvalidSecretException("message")

            it("should create error message") {

                val errorMessage = exceptionHandler
                    .handleInvalidSecretException(exception)

                assertThat(errorMessage.message, equalTo(exception.message))
                assertThat(errorMessage.status, equalTo(HttpStatus.FORBIDDEN))
            }
        }
    }
})
