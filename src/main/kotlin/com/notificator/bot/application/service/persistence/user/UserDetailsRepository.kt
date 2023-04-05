package com.notificator.bot.application.service.persistence.user

import com.notificator.bot.application.service.persistence.user.entity.UserDetailsEntity
import org.springframework.data.repository.CrudRepository

interface UserDetailsRepository: CrudRepository<UserDetailsEntity, Long> {

}