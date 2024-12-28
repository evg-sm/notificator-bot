package com.notificator.bot.adapter.persistence.notification

import com.notificator.bot.adapter.persistence.notification.entity.NotificationEntity
import com.notificator.bot.application.port.out.NotificationStoragePort
import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.NotificationSendStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Repository
class NotificationStorageAdapter(
    private val repository: NotificationRepository
) : NotificationStoragePort {

    override fun save(notificationDraft: NotificationDraft) {
        repository.save(notificationDraft.toEntity())
    }

    override fun save(notification: Notification) {
        repository.save(notification.toEntity())
    }

    override fun selectUnsentWithLock(): List<Notification> =
        repository.selectUnsentWithLock(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)).map { it.toDomain() }

    override fun findUnsentByUserId(userID: Long): List<Notification> =
        repository.findByUserIdAndSendStatus(userID, NotificationSendStatus.PENDING).map { it.toDomain() }
            .filter { it.sendTime >= LocalDateTime.now() }

    override fun deleteByNotificationId(id: Long) {
        repository.deleteById(id)
    }

    private fun NotificationDraft.toEntity(): NotificationEntity = NotificationEntity(
        userId = userId,
        chatId = chatId,
        type = type,
        sendStatus = NotificationSendStatus.PENDING,
        text = String(text.toByteArray(), Charsets.UTF_8),
        sendTime = LocalDateTime.of(date, time),
        createTime = LocalDateTime.now(),
        updateTime = LocalDateTime.now()
    )

    private fun Notification.toEntity(): NotificationEntity = NotificationEntity(
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

    private fun NotificationEntity.toDomain(): Notification = Notification(
        id = id,
        userId = userId,
        chatId = chatId,
        type = type,
        sendStatus = sendStatus,
        text = text,
        sendTime = sendTime
    )
}
