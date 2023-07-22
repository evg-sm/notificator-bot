package com.notificator.bot.cucumber.step

import com.notificator.bot.cucumber.helper.WebClientHelper
import io.cucumber.java8.En
import org.springframework.http.HttpMethod

class UIStepDefinitions(
    private val webClientHelper: WebClientHelper
) : En {
    init {
        When("User request home page") {
            webClientHelper.send(HttpMethod.GET, "/")
        }
    }
}