package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.ErrorMessage
import com.github.matek2305.izardbets.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import spock.lang.Specification

class RestExceptionHandlerSpec extends Specification {

    private RestExceptionHandler exceptionHandler = new RestExceptionHandler()

    def "should handle resource not found exception"() {
        given:
            def exception = new ResourceNotFoundException('message')
        when:
            ErrorMessage errorMessage = exceptionHandler.handleResourceNotFoundException(exception)
        then:
            errorMessage.message == exception.message
            errorMessage.status == HttpStatus.NOT_FOUND
    }
}
