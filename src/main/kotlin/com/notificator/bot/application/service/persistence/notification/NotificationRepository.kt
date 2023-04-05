package com.notificator.bot.application.service.persistence.notification

import com.notificator.bot.application.service.persistence.notification.entity.NotificationEntity
import org.springframework.data.repository.CrudRepository

interface NotificationRepository: CrudRepository<NotificationEntity, Long> {
}
