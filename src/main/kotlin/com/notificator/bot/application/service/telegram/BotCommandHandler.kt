package com.notificator.bot.application.service.telegram

import com.notificator.bot.application.service.notification.NotificationBuildHandler
import com.notificator.bot.application.service.telegram.components.BotCommands
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.COMMAND_KEYWORD_LIST
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.HELP_KEYWORD
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.HELP_TEXT
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.START_KEYWORD
import com.notificator.bot.application.service.telegram.components.Buttons
import com.notificator.bot.domain.UserDetails
import mu.KLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

interface BotCommandHandler {
    fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit)
}

@Component
class BotCommandHandlerImpl(
    private val notificationBuildHandler: NotificationBuildHandler
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
            else -> "Пожалуйста, введите корректную команду ${listOf(START_KEYWORD, HELP_KEYWORD)}"
        }

        execute(SendMessage().apply {
            chatId = update.message.chatId.toString()
            text = responseText
        })
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
}
