package com.notificator.bot.domain

import java.time.LocalDate
import java.time.LocalTime

data class Notification(
    val id: Long? =null,
    val status: Status,
    val text: String,
    val date: LocalDate,
    val time: LocalTime
)

enum class Status {
    PENDING,
    SENT;
}
