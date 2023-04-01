package com.notificator.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotificatorBotApp

fun main(args: Array<String>) {
        runApplication<NotificatorBotApp>(*args)
}
