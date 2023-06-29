package com.notificator.bot.application.port.out

import com.notificator.bot.domain.UserDetails

interface UserDetailStoragePort {

    fun save(userDetails: UserDetails)

    fun findById(id: Long): UserDetails?
}
