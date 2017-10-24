package com.github.matek2305.izardbets.api

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorMessage(
    val status: HttpStatus,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
