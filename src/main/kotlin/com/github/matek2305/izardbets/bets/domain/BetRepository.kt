package com.github.matek2305.izardbets.bets.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BetRepository : ReactiveMongoRepository<Bet, String>
