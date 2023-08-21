package com.notificator.bot.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.telegram")
data class BotSetting(
    val token: String,
    val botUsername: String,
    val uiHost: String
)
