package com.notificator.bot.domain

import java.time.LocalDate
import java.time.LocalTime

data class NotificationDraft(
    val chatId: String,
    val type: NotificationType,
    val userId: Long,
    val draftState: DraftState,
    val text: String,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    var monthCounter: Long = 0
)

enum class DraftState {
    INIT,
    TYPE_SET,
    DATE_SET,
    TIME_SET;
}
