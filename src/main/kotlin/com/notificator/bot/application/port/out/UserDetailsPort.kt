package com.notificator.bot.application.port.out

import com.notificator.bot.domain.UserDetails

interface UserDetailsPort {

    fun save(userDetails: UserDetails)

    fun findById(id: Long): UserDetails?
}
