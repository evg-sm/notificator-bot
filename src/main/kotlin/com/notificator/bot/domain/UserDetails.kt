package com.notificator.bot.domain

data class UserDetails(
    val id: Long,
    val firstName: String,
    val userName: String? = null
)
