package com.github.matek2305.izardbets.competitions.factory

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Component

@Component
class InvitationCodeGenerator {

    fun generate(): String = RandomStringUtils.randomAlphanumeric(8)
}
