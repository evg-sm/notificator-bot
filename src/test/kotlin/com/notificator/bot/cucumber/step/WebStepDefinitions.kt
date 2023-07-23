package com.notificator.bot.cucumber.step

import com.notificator.bot.cucumber.helper.DbHelper
import com.notificator.bot.cucumber.helper.WebClientHelper
import com.notificator.bot.default_user_id
import io.cucumber.java8.En
import org.springframework.http.HttpMethod

class WebStepDefinitions(
    private val webClientHelper: WebClientHelper,
    private val dbHelper: DbHelper
) : En {
    init {

        When("User request notification list page") {
            webClientHelper.send(HttpMethod.GET, "/list/$default_user_id")
        }

        When("User request home page") {
            webClientHelper.send(HttpMethod.GET, "/")
        }
    }
}
