package com.notificator.bot.cucumber.step

object ScenarioContext {
    val notificationName: ThreadLocal<String> = ThreadLocal()
    val notificationType: ThreadLocal<String> = ThreadLocal()
}
