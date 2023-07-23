package com.notificator.bot.cucumber.step

import com.notificator.bot.cucumber.helper.DbHelper
import io.cucumber.java8.En
import io.cucumber.java8.Scenario

class Hooks(
    private val dbHelper: DbHelper
    ): En {
        init {
            Before("@CleanNotificationTable") { _: Scenario ->
                dbHelper.clearTables("bot.notification")
            }
        }
}