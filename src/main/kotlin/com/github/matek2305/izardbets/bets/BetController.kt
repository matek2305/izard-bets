package com.github.matek2305.izardbets.bets

import com.github.matek2305.izardbets.bets.api.CreateBetCommand
import com.github.matek2305.izardbets.bets.domain.Bet
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class BetController(private val betService: BetService) {

    @PostMapping("/bets")
    fun createBet(@RequestBody command: Mono<CreateBetCommand>): Mono<ResponseEntity<Bet>> {
        return command
            .flatMap { betService.create(it) }
            .map { ResponseEntity(it, HttpStatus.CREATED) }
            .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }
}
