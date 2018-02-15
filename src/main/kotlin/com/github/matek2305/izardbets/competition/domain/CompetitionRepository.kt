package com.github.matek2305.izardbets.competition.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CompetitionRepository : ReactiveMongoRepository<Competition, String>
