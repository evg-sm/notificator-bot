package com.notificator.bot.cucumber.step

import com.notificator.bot.cucumber.helper.WebClientHelper
import io.cucumber.java8.En

class WebClientStepDefinitions(
    private val webClientHelper: WebClientHelper
) : En {
    init {

        Then("User check response status code {int}") { statusCode: Int ->
            webClientHelper.expectStatus(statusCode)
        }

        Then("User check response body from file {bodyPath}") { bodyValue: String ->
            webClientHelper.expectBodyXml(bodyValue)
        }
    }
}