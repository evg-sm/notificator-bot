package com.notificator.bot.application.port.out

import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationDraft

interface NotificationStoragePort {
    fun save(notificationDraft: NotificationDraft)
    fun save(notification: Notification)
    fun selectUnsentWithLock(): List<Notification>
    fun findUnsentByUserId(userID: Long): List<Notification>
    fun deleteByNotificationId(id: Long)
}
