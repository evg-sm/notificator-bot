package com.notificator.bot.application.service

import com.notificator.bot.application.port.out.NotificationStoragePort
import com.notificator.bot.application.port.out.NotificationQuery
import com.notificator.bot.domain.Notification
import org.springframework.stereotype.Component

@Component
class NotificationQueryImpl(
    private val notificationStoragePort: NotificationStoragePort
): NotificationQuery {

    override fun get(userId: Long): List<Notification> =
        notificationStoragePort.findByUserId(userId)
}
