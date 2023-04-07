package com.notificator.bot.application.service.persistence.notification.entity

import com.notificator.bot.application.service.persistence.user.entity.UserDetailsEntity
import com.notificator.bot.domain.NotificationSendStatus
import com.notificator.bot.domain.NotificationType
import org.springframework.data.util.ProxyUtils
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "bot", name = "notification")
data class NotificationEntity(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bot.notification_id")
    val id: Long? = null,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "chat_id")
    val chatId: String,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: NotificationType,

    @Enumerated(EnumType.STRING)
    @Column(name = "send_status")
    val sendStatus: NotificationSendStatus,

    @Column(name = "text")
    val text: String,

    @Column(name = "date_time")
    val dateTime: LocalDateTime,
) {
    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as UserDetailsEntity

        return this.id == other.id
    }

    override fun hashCode() = 45
}
