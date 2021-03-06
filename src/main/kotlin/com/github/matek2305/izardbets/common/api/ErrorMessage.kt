package com.github.matek2305.izardbets.common.api

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@JsonTypeName("error")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
data class ErrorMessage(
    val status: HttpStatus,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
