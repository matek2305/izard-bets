package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.exception.ResourceNotFoundException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.springframework.http.HttpStatus

object RestExceptionHandlerSpec : Spek({

    given("rest exception handler") {
        val exceptionHandler = RestExceptionHandler()
        on("resource not found exception occurs") {
            val exception = ResourceNotFoundException("message")
            val errorMessage = exceptionHandler.handleResourceNotFoundException(exception)
            it("should create error message") {
                assertEquals(exception.message, errorMessage.message)
                assertEquals(HttpStatus.NOT_FOUND, errorMessage.status)
            }
        }
    }
})
