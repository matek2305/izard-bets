package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.ErrorMessage
import com.github.matek2305.izardbets.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class RestExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(exception: ResourceNotFoundException) : ErrorMessage {
        return ErrorMessage(HttpStatus.NOT_FOUND, exception.message!!)
    }
}
