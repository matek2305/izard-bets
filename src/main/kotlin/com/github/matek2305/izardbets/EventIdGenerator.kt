package com.github.matek2305.izardbets

import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class EventIdGenerator {

    fun generate(): String = ObjectId().toString()
}
