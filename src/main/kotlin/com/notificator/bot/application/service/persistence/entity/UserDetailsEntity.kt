package com.notificator.bot.application.service.persistence.entity

import org.springframework.data.util.ProxyUtils
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "bot", name = "user_details")
data class UserDetailsEntity(
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "user_name")
    val userName: String? = null
) {

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as UserDetailsEntity

        return this.id == other.id
    }

    override fun hashCode() = 33

}
