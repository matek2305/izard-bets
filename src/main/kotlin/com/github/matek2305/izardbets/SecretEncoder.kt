package com.github.matek2305.izardbets

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class SecretEncoder {

    fun encode(secret: String): String = BCrypt.hashpw(secret, BCrypt.gensalt())

    fun check(secret: String, encodedSecret: String): Boolean = BCrypt.checkpw(secret, encodedSecret)
}
