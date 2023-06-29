package com.notificator.bot.adapter.persistence.user

import com.notificator.bot.application.port.out.UserDetailStoragePort
import com.notificator.bot.adapter.persistence.user.entity.UserDetailsEntity
import com.notificator.bot.domain.UserDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserDetailStorageAdapter(
    private val repository: UserDetailsRepository
): UserDetailStoragePort {

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
