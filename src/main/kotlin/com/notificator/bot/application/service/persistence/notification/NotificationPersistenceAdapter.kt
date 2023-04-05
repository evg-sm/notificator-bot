package com.notificator.bot.application.service.persistence.notification

import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.service.persistence.notification.entity.NotificationEntity
import com.notificator.bot.domain.Notification
import org.springframework.stereotype.Repository

@Repository
class NotificationPersistenceAdapter(
    private val repository: NotificationRepository
) : NotificationPersistencePort {

    override fun save(notification: Notification) {
        repository.save(notification.toEntity())
    }

    private fun Notification.toEntity() = NotificationEntity(
        id = id,
        status = status,
        text = text,
        date = date,
        time = time
    )
}
