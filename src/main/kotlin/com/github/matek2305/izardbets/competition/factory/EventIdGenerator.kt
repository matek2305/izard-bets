package com.github.matek2305.izardbets.competition.factory

import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class EventIdGenerator {

    fun generate(): String = ObjectId().toString()
}
