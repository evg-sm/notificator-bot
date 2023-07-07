package com.notificator.bot.adapter.web.dto

data class NotificationDto(
    val id: Long,
    val userId: Long,
    val chatId: String,
    val type: String,
    val sendStatus: String,
    val text: String,
    val sendTime: String
)
