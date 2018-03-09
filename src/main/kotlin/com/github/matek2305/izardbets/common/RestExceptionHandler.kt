package com.github.matek2305.izardbets.common

import com.github.matek2305.izardbets.common.api.ErrorMessage
import com.github.matek2305.izardbets.common.exception.InvalidSecretException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException

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
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(exception: WebExchangeBindException): ErrorMessage {
        return ErrorMessage(HttpStatus.BAD_REQUEST, retrieveErrorMessage(exception))
    }

    private fun retrieveErrorMessage(exception: WebExchangeBindException): String {
        return exception.bindingResult.fieldErrors
            .map { it.defaultMessage }
            .first() ?: exception.message!!
    }
}
