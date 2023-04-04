package com.notificator.bot.application.service.persistence

import com.notificator.bot.application.service.persistence.entity.UserDetailsEntity
import org.springframework.data.repository.CrudRepository

interface UserDetailsRepository: CrudRepository<UserDetailsEntity, Long> {

}