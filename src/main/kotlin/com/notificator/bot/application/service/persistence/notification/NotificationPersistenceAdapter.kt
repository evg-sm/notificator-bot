package com.notificator.bot.application.service.persistence.notification

import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.service.persistence.notification.entity.NotificationEntity
import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.NotificationSendStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Repository
class NotificationPersistenceAdapter(
    private val repository: NotificationRepository
) : NotificationPersistencePort {

    override fun save(notificationDraft: NotificationDraft) {
        repository.save(notificationDraft.toEntity())
    }

    override fun save(notification: Notification) {
        repository.save(notification.toEntity())
    }

    override fun selectUnsent(): List<Notification> =
        repository.selectUnsent(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)).map { it.toDomain() }

    override fun findByUserId(userID: Long): List<Notification> =
        repository.findByUserId(userID).map { it.toDomain() }


    private fun NotificationDraft.toEntity() = NotificationEntity(
        userId = userId,
        chatId = chatId,
        type = type,
        sendStatus = NotificationSendStatus.PENDING,
        text = text,
        sendTime = LocalDateTime.of(date, time),
        createTime = LocalDateTime.now(),
        updateTime = LocalDateTime.now()
    )

    private fun Notification.toEntity() = NotificationEntity(
        id = id,
        userId = userId,
        chatId = chatId,
        type = type,
        sendStatus = sendStatus,
        text = text,
        sendTime = sendTime,
        createTime = LocalDateTime.now(),
        updateTime = LocalDateTime.now()
    )

    private fun NotificationEntity.toDomain() = Notification(
        id = id,
        userId = userId,
        chatId = chatId,
        type = type,
        sendStatus = sendStatus,
        text = text,
        sendTime = sendTime
    )
}
