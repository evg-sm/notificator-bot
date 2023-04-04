package com.notificator.bot.application.service.persistence

import com.notificator.bot.application.port.out.UserDetailsPort
import com.notificator.bot.application.service.persistence.entity.UserDetailsEntity
import com.notificator.bot.domain.UserDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserDetailsAdapter(
    private val repository: UserDetailsRepository
): UserDetailsPort {

    override fun save(userDetails: UserDetails) {
        if (findById(userDetails.id) == null) {
            repository.save(userDetails.toEntity())
        }
    }

    override fun findById(id: Long): UserDetails? {
        return repository.findByIdOrNull(id)?.toDomain()
    }

    private fun UserDetails.toEntity() = UserDetailsEntity(
        id = id,
        firstName = firstName,
        userName = userName
    )

    private fun UserDetailsEntity.toDomain() = UserDetails(
        id = id,
        firstName = firstName,
        userName = userName
    )
}
