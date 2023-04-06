package com.notificator.bot.application.service.persistence.notification

import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.service.persistence.notification.entity.NotificationEntity
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.NotificationStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class NotificationPersistenceAdapter(
    private val repository: NotificationRepository
) : NotificationPersistencePort {

    override fun save(notificationDraft: NotificationDraft) {
        repository.save(notificationDraft.toEntity())
    }

    private fun NotificationDraft.toEntity() = NotificationEntity(
        id = id,
        userId = userId,
        type = type,
        status = NotificationStatus.PENDING,
        text = text,
        dateTime = LocalDateTime.of(date, time)
    )
}
