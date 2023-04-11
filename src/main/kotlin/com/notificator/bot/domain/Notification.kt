package com.notificator.bot.domain

import java.time.LocalDateTime

data class Notification(
    val id: Long? = null,
    val userId: Long,
    val chatId: String,
    val type: NotificationType,
    val sendStatus: NotificationSendStatus,
    val text: String,
    val sendTime: LocalDateTime
)
