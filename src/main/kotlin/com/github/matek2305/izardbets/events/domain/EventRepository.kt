package com.github.matek2305.izardbets.events.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : ReactiveMongoRepository<Event, String>
