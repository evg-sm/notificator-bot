package com.notificator.bot.cucumber.step

import com.notificator.bot.cucumber.helper.DbHelper
import com.notificator.bot.cucumber.helper.WebClientHelper
import com.notificator.bot.default_chat_id
import com.notificator.bot.default_user_id
import io.cucumber.java8.En
import org.springframework.http.HttpMethod

class UIStepDefinitions(
    private val webClientHelper: WebClientHelper,
    private val dbHelper: DbHelper
) : En {
    init {
        Given("User insert notification into DB") {
            dbHelper.insert("""
                insert into bot.notification (user_id, chat_id, type, send_status, text, send_time)
                values ($default_user_id, $default_chat_id, 'EVERY_YEAR', 'PENDING', 'TEST', '2024-04-22 12:00:00')
                """)
        }

        When("User request notification list page") {
            webClientHelper.send(HttpMethod.GET, "/list/$default_user_id")
        }

        When("User request home page") {
            webClientHelper.send(HttpMethod.GET, "/")
        }
    }
}