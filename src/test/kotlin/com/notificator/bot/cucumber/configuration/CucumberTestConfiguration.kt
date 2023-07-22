package com.notificator.bot.cucumber.configuration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient

@TestConfiguration
class CucumberTestConfiguration {

    @Bean
    fun webTestClient(context: ServletWebServerApplicationContext): WebTestClient =
        WebTestClient.bindToServer().baseUrl("http://localhost:${context.webServer.port}").build()
}
