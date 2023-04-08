package com.notificator.bot.application.service.notification

import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.port.out.NotificationQuery
import com.notificator.bot.domain.Notification
import org.springframework.stereotype.Component

@Component
class NotificationQueryImpl(
    private val notificationPersistencePort: NotificationPersistencePort
): NotificationQuery {

    override fun get(userId: Long): List<Notification> =
        notificationPersistencePort.findByUserId(userId)
}
