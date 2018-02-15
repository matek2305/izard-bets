package com.github.matek2305.izardbets.competitions.domain

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CompetitionRepository : ReactiveMongoRepository<Competition, String> {

    @Query("{ 'id': ?0, 'events.id': ?1 }")
    fun findByIdAndEventId(id: String, eventId: String): Mono<Competition>
}
