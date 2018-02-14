package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.CreateBetCommand
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class EventBetController {

    @PostMapping("/event_bets")
    @ResponseStatus(HttpStatus.CREATED)
    fun createBet(@RequestBody command: Mono<CreateBetCommand>) {
        TODO("not implemented yet :)")
    }
}
