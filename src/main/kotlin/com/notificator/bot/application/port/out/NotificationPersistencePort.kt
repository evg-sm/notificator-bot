package com.notificator.bot.application.port.out

import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationDraft

interface NotificationPersistencePort {
    fun save(notificationDraft: NotificationDraft)
    fun save(notification: Notification)
    fun selectUnsent(): List<Notification>
    fun findByUserId(userID: Long): List<Notification>
}
