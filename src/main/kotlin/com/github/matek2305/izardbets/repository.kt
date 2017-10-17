package com.github.matek2305.izardbets

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CompetitionRepository : ReactiveMongoRepository<Competition, String>

interface EventBetRepository : ReactiveMongoRepository<EventBet, String>
