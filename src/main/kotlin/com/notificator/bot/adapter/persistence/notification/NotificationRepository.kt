package com.notificator.bot.adapter.persistence.notification

import com.notificator.bot.adapter.persistence.notification.entity.NotificationEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface NotificationRepository : CrudRepository<NotificationEntity, Long> {

    @Query(
        value =
        "select n.* from bot.notification n where n.send_status='PENDING' and n.send_time = :now for update skip locked",
        nativeQuery = true
    )
    fun selectUnsent(now: LocalDateTime): List<NotificationEntity>

    fun findByUserId(userId: Long): List<NotificationEntity>
}
