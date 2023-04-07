package com.notificator.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class NotificatorBotApp

fun main(args: Array<String>) {
    runApplication<NotificatorBotApp>(*args)
}
