package com.notificator.bot.application.port.out

import com.notificator.bot.domain.Notification

interface NotificationQuery {
    fun get(userId: Long): List<Notification>
}