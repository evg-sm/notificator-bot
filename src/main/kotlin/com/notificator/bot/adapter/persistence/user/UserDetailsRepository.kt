package com.notificator.bot.adapter.persistence.user

import com.notificator.bot.adapter.persistence.user.entity.UserDetailsEntity
import org.springframework.data.repository.CrudRepository

interface UserDetailsRepository: CrudRepository<UserDetailsEntity, Long> {

}