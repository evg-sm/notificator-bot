package com.notificator.bot.adapter.telegram

import com.notificator.bot.adapter.telegram.components.BotCommands
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.ASK_FOR_NOTIFICATION_TEXT
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.CANCEL_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.COMMAND_KEYWORD_LIST
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.EDIT_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.HELP_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.HELP_TEXT
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.LIST_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.START_KEYWORD
import com.notificator.bot.application.configuration.BotSetting
import com.notificator.bot.application.port.out.DraftNotificationStoragePort
import com.notificator.bot.application.port.out.NotificationSenderPort
import com.notificator.bot.application.port.out.NotificationService
import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationSendStatus
import com.notificator.bot.domain.NotificationType
import mu.KLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface BotCommandHandler {
    fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit)
}

@Component
class BotCommandHandlerImpl(
    private val notificationService: NotificationService,
    private val draftStoragePort: DraftNotificationStoragePort,
    private val notificationSenderPort: NotificationSenderPort,
    private val botSetting: BotSetting
) : BotCommandHandler, BotCommands {

    companion object : KLogging()

    override fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        when (update.message.text) {
            START_KEYWORD -> sendCommandResponse(update, ASK_FOR_NOTIFICATION_TEXT)
            HELP_KEYWORD -> sendCommandResponse(update, HELP_TEXT)
            LIST_KEYWORD -> sendCommandResponse(update, getUserNotifications(update.message.from.id))
            CANCEL_KEYWORD -> sendCommandResponse(update, "Отмена")
            EDIT_KEYWORD -> sendEditLink(update)
            else -> sendCommandResponse(update, "Пожалуйста, введите корректную команду $COMMAND_KEYWORD_LIST}")
        }.also {
            draftStoragePort.invalidateByUserId(update.message.from.id)
        }
    }

    private fun sendCommandResponse(update: Update, responseText: String) {
        notificationSenderPort.sendMessage(
            toChatId = update.message.chatId.toString(),
            messageText = responseText
        )
    }

    private fun sendEditLink(update: Update) {
        notificationSenderPort.sendMessageAsLink(
            toChatId = update.message.chatId.toString(),
            messageText = "${botSetting.uiHost}/list/${update.message.from.id}"
        )
    }

    private fun getUserNotifications(userId: Long): String {
        return notificationService.findByUserId(userId).filter { ntf ->
            ntf.sendStatus != NotificationSendStatus.SENT
                    && ntf.sendTime.toLocalDate() >= LocalDate.now()
        }.prettyNotificationList()
    }

    private fun List<Notification>.prettyNotificationList(): String {
        return if (isNotEmpty()) {
            val mapByType: Map<NotificationType, List<Notification>> =
                associateBy({ it.type }, { filter { n -> it.type == n.type } })

            val stringBuilder = StringBuilder()
            mapByType.keys.forEach { type -> appendEach(stringBuilder, type, mapByType.getValue(type)) }

            stringBuilder.toString()
        } else {
            "У Bас пока нет сохраненных уведомлений"
        }
    }

    private fun appendEach(
        stringBuilder: StringBuilder,
        type: NotificationType,
        notifications: List<Notification>
    ): StringBuilder {
        if (notifications.isNotEmpty()) {
            stringBuilder.append("\n").append("${type.pretty()}:").append("\n").append("\n")
            notifications.forEach { n ->
                stringBuilder.append("'${n.text}' '${n.sendTime.pretty()}'").append("\n")
            }
        }
        return stringBuilder
    }

    private fun NotificationType.pretty(): String = when (this) {
        NotificationType.ONCE -> "Единоразовые"
        NotificationType.EVERY_DAY -> "Каждый день"
        NotificationType.EVERY_WEEK -> "Каждую неделю"
        NotificationType.EVERY_MONTH -> "Каждый месяц"
        NotificationType.EVERY_YEAR -> "Каждый год"
        NotificationType.UNDEFINED -> "Не определено"
    }

    private fun LocalDateTime.pretty(): String = format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}
