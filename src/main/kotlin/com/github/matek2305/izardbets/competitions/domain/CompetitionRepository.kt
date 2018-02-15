package com.github.matek2305.izardbets.competitions.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CompetitionRepository : ReactiveMongoRepository<Competition, String>
