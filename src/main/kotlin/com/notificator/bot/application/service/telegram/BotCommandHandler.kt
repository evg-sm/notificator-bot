package com.notificator.bot.application.service.telegram

import com.notificator.bot.application.port.out.NotificationDraftStoragePort
import com.notificator.bot.application.port.out.NotificationQuery
import com.notificator.bot.application.service.notification.NotificationBuildHandler
import com.notificator.bot.application.service.telegram.components.BotCommands
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.COMMAND_KEYWORD_LIST
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.HELP_KEYWORD
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.HELP_TEXT
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.LIST_KEYWORD
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.START_KEYWORD
import com.notificator.bot.application.service.telegram.components.Buttons
import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.NotificationType
import mu.KLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface BotCommandHandler {
    fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit)
}

@Component
class BotCommandHandlerImpl(
    private val notificationBuildHandler: NotificationBuildHandler,
    private val notificationQuery: NotificationQuery,
    private val draftStoragePort: NotificationDraftStoragePort
) : BotCommandHandler, BotCommands {

    companion object : KLogging()

    override fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        when {
            update.isCommandMessage() -> handleCommandMessage(update, execute)
            update.isTextMessage() -> handleTextMessage(update, execute)
        }
    }

    private fun Update.isCommandMessage() = hasMessage() && message.hasText() && message.text in COMMAND_KEYWORD_LIST

    private fun handleCommandMessage(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        val responseText = when (update.message.text) {
            START_KEYWORD -> "О чем Вам напомнить?"
            HELP_KEYWORD -> HELP_TEXT
            LIST_KEYWORD -> prettyNotificationList(notificationQuery.get(update.message.from.id))
            else -> "Пожалуйста, введите корректную команду $COMMAND_KEYWORD_LIST}"
        }

        execute(SendMessage().apply {
            chatId = update.message.chatId.toString()
            text = responseText
        }).also {
            draftStoragePort.clear(update.message.from.id)
        }
    }

    private fun Update.isCallbackQuery() = hasCallbackQuery()

    private fun Update.isTextMessage() = hasMessage() && message.hasText()

    // todo delete it !!!
    private fun createResponse(update: Update, responseText: String) = SendMessage().apply {
        chatId = update.message.chatId.toString()
        text = responseText
        replyMarkup = Buttons.inlineMarkup()
    }

    private fun handleTextMessage(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        logger.info { "Received text message ${update.message.text}" }
        notificationBuildHandler.handle(update, execute)
    }

    private fun prettyNotificationList(list: List<Notification>): String {
        return if (list.isNotEmpty()) {
            var rowNum = 1
            val stringBuilder = StringBuilder()
            list.forEach { ntf: Notification ->
                stringBuilder
                    .append("$rowNum - '${ntf.text}' '${ntf.type.pretty()}' '${ntf.dateTime.pretty()}'")
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
