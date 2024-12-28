package com.notificator.bot.adapter.web

import com.notificator.bot.adapter.web.dto.NotificationDto
import com.notificator.bot.application.port.out.NotificationService
import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationSendStatus
import com.notificator.bot.domain.NotificationType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/")
    fun index(): String = "index.html"

    @GetMapping("/list/{userId}")
    fun notificationList(@PathVariable userId: Long, model: Model): String {
        model.addAttribute("notifications", notificationService.findByUserId(userId).sorted().toDto())
        model.addAttribute("userId", userId)
        return "notifications"
    }

    @PostMapping("/delete")
    fun delete(
        @RequestParam notificationId: Long,
        @RequestParam userId: Long,
        model: Model
    ): String {
        notificationService.deleteByNotificationId(notificationId)
        model.addAttribute("notifications", notificationService.findByUserId(userId).toDto())
        return "notifications"
    }

    private fun List<Notification>.toDto(): List<NotificationDto> = map { ntf ->
        NotificationDto(
            id = ntf.id ?: 0L,
            userId = ntf.userId,
            chatId = ntf.chatId,
            type = ntf.type.toDto(),
            sendStatus = ntf.sendStatus.toDto(),
            text = ntf.text,
            sendTime = ntf.sendTime.formatAsPattern(),
        )
    }

    private fun NotificationType.toDto(): String = when (this) {
        NotificationType.ONCE -> "Одноразовое"
        NotificationType.EVERY_DAY -> "Каждый день"
        NotificationType.EVERY_WEEK -> "Каждую неделю"
        NotificationType.EVERY_MONTH -> "Каждый месяц"
        NotificationType.EVERY_YEAR -> "Каждый год"
        else -> "Неизвестный тип уведомления"
    }

    private fun NotificationSendStatus.toDto(): String = when (this) {
        NotificationSendStatus.PENDING -> "Ожидает отправки"
        NotificationSendStatus.SENT -> "Отправлено"
    }

    private fun LocalDateTime.formatAsPattern() = format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
}
