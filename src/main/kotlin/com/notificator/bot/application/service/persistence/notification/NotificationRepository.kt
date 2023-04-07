package com.notificator.bot.application.service.persistence.notification

import com.notificator.bot.application.service.persistence.notification.entity.NotificationEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface NotificationRepository : CrudRepository<NotificationEntity, Long> {

    @Query(
        value =
        "select n.* from bot.notification n where n.send_status='PENDING' and n.date_time = :now for update",
        nativeQuery = true
    )
    fun selectUnsent(now: LocalDateTime): List<NotificationEntity>
}
