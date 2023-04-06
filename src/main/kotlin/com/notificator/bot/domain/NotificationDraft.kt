package com.notificator.bot.domain

import java.time.LocalDate
import java.time.LocalTime

data class NotificationDraft(
    val id: Long? = null,
    val type: NotificationType,
    val userId: Long,
    val state: State,
    val text: String,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now()
)

enum class State {
    INIT,
    TYPE_SET,
    DATE_SET,
    TIME_SET;
}
