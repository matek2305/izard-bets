package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.ErrorMessage
import com.github.matek2305.izardbets.exception.InvalidSecretException
import com.github.matek2305.izardbets.exception.ValidationFailedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class RestExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidSecretException::class)
    fun handleInvalidSecretException(exception: InvalidSecretException): ErrorMessage {
        return ErrorMessage(HttpStatus.FORBIDDEN, exception.message!!)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationFailedException::class)
    fun handleValidationFailedException(exception: ValidationFailedException): ErrorMessage {
        return ErrorMessage(HttpStatus.BAD_REQUEST, exception.message!!)
    }
}
