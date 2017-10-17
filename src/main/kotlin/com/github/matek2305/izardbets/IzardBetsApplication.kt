package com.github.matek2305.izardbets

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@SpringBootApplication
class IzardBetsApplication {

    @Bean
    fun runner(
        competitionRepository: CompetitionRepository,
        eventBetRepository: EventBetRepository) = ApplicationRunner {

        val mciVsNap = Event(
            homeTeamName = "Manchester City",
            awayTeamName = "Napoli",
            date = LocalDateTime.of(2017, Month.OCTOBER, 17, 20, 45))

        val rmaVsTot = Event(
            homeTeamName = "Real Madrid",
            awayTeamName = "Tottenham",
            date = LocalDateTime.of(2017, Month.OCTOBER, 17, 20, 45))

        val championsLeague = Competition(
            name = "Champions League 2017/18",
            events = arrayOf(mciVsNap, rmaVsTot))

        val random = Random()

        competitionRepository
            .deleteAll()
            .thenMany(Flux.just(championsLeague).flatMap { competitionRepository.save(it) })
            .thenMany(competitionRepository.findAll())
            .subscribe({ println(it) })
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(IzardBetsApplication::class.java, *args)
}

fun Random.nextInt(range: IntRange): Int {
    return range.start + nextInt(range.last - range.start)
}
