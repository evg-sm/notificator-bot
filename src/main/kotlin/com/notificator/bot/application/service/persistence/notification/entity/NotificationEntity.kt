package com.notificator.bot.application.service.persistence.notification.entity

import com.notificator.bot.application.service.persistence.user.entity.UserDetailsEntity
import com.notificator.bot.domain.Status
import org.springframework.data.util.ProxyUtils
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(schema = "bot", name = "notification")
data class NotificationEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bot.notification_id")
    val id: Long? = null,
    @Column(name = "status")
    val status: Status,
    @Column(name = "text")
    val text: String,
    @Column(name = "date")
    val date: LocalDate,
    @Column(name = "time")
    val time: LocalTime
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
