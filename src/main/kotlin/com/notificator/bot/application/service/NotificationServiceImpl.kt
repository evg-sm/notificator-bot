package com.notificator.bot.application.service

import com.notificator.bot.application.port.out.NotificationService
import com.notificator.bot.application.port.out.NotificationStoragePort
import com.notificator.bot.domain.Notification
import org.springframework.stereotype.Component

@Component
class NotificationServiceImpl(
    private val notificationStoragePort: NotificationStoragePort
) : NotificationService {

    override fun findByUserId(userId: Long): List<Notification> = notificationStoragePort.findUnsentByUserId(userId)

    override fun deleteByNotificationId(id: Long) = notificationStoragePort.deleteByNotificationId(id)
}
