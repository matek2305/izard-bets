package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.domain.EventBet
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CompetitionRepository : ReactiveMongoRepository<Competition, String>

interface EventBetRepository : ReactiveMongoRepository<EventBet, String>
