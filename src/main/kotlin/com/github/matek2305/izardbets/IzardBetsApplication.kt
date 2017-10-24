package com.github.matek2305.izardbets

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication
class IzardBetsApplication

fun main(args: Array<String>) {
    SpringApplication.run(IzardBetsApplication::class.java, *args)
}
