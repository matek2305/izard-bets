package com.github.matek2305.izardbets.common

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class SecretEncoderSpec : Spek({

    describe("secret encoder") {

        val secretEncoder = SecretEncoder()

        it("should encode secret") {
            val secret = "secret"
            assertThat(secretEncoder.encode(secret), !equalTo(secret))
        }

        it("should generate different hash for subsequent encodes") {
            val secret = "secret"
            val firstEncode = secretEncoder.encode(secret)
            val secondEncode = secretEncoder.encode(secret)
            assertThat(firstEncode, !equalTo(secondEncode))
        }

        it("should be able to verify secret with hash") {
            val secret = "secret"
            val encoded = secretEncoder.encode(secret)
            assertThat(secretEncoder.check(secret, encoded), equalTo(true))
        }
    }
})
