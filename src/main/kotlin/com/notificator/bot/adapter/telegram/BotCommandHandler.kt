package com.notificator.bot.adapter.telegram

import com.notificator.bot.application.port.out.NotificationDraftStoragePort
import com.notificator.bot.application.port.out.NotificationQuery
import com.notificator.bot.application.port.out.NotificationSenderPort
import com.notificator.bot.adapter.telegram.components.BotCommands
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.ASK_FOR_NOTIFICATION_TEXT
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.CANCEL_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.COMMAND_KEYWORD_LIST
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.HELP_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.HELP_TEXT
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.LIST_KEYWORD
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.START_KEYWORD
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
    private val notificationQuery: NotificationQuery,
    private val draftStoragePort: NotificationDraftStoragePort,
    private val notificationSenderPort: NotificationSenderPort
) : BotCommandHandler, BotCommands {

    companion object : KLogging()

    override fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        val responseText = when (update.message.text) {
            START_KEYWORD -> ASK_FOR_NOTIFICATION_TEXT
            HELP_KEYWORD -> HELP_TEXT
            LIST_KEYWORD -> getUserNotifications(update.message.from.id)
            CANCEL_KEYWORD -> "Отмена"
            else -> "Пожалуйста, введите корректную команду $COMMAND_KEYWORD_LIST}"
        }.also {
            draftStoragePort.clear(update.message.from.id)
        }
        notificationSenderPort.sendMessage(
            toChatId = update.message.chatId.toString(),
            messageText = responseText
        )
    }

    private fun getUserNotifications(userId: Long) : String {
        return notificationQuery.get(userId).filter {
                ntf -> ntf.sendStatus != NotificationSendStatus.SENT
                && ntf.sendTime.toLocalDate() >= LocalDate.now()
        }.prettyNotificationList()
    }

    private fun List<Notification>.prettyNotificationList(): String {
        return if (isNotEmpty()) {
            var rowNum = 1
            val stringBuilder = StringBuilder()
            forEach { ntf: Notification ->
                stringBuilder
                    .append("$rowNum - '${ntf.text}' '${ntf.type.pretty()}' '${ntf.sendTime.pretty()}'")
                    .append("\n")
                rowNum++
            }
            stringBuilder.toString()
        } else {
            "У Bас пока нет сохраненных уведомлений"
        }
    }

    private fun NotificationType.pretty(): String = when (this) {
        NotificationType.REGULAR -> "регулярное"
        NotificationType.ONCE -> "единоразовое"
        NotificationType.UNDEFINED -> "не определено"
    }

    private fun LocalDateTime.pretty(): String = format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}
