package com.notificator.bot.application.service.persistence.notification.entity

import com.notificator.bot.application.service.persistence.user.entity.UserDetailsEntity
import com.notificator.bot.domain.NotificationSendStatus
import com.notificator.bot.domain.NotificationType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.util.ProxyUtils
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "bot", name = "notification")
@EntityListeners(AuditingEntityListener::class)
data class NotificationEntity(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "send_time")
    val sendTime: LocalDateTime,

    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    val createTime: LocalDateTime,

    @LastModifiedDate
    @Column(name = "update_time", nullable = false, updatable = true)
    val updateTime: LocalDateTime
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
