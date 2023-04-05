package com.notificator.bot.application.port.out

import com.notificator.bot.domain.Notification

interface NotificationPersistencePort {
    fun save(notification: Notification)
}
