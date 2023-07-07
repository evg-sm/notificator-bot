package com.notificator.bot.application.port.out

import com.notificator.bot.domain.Notification

interface NotificationService {
    fun findByUserId(userId: Long): List<Notification>
    fun deleteByNotificationId(id: Long)
}