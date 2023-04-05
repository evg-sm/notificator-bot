package com.notificator.bot.application.service.telegram

import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.port.out.UserDetailsPersistencePort
import com.notificator.bot.application.service.telegram.components.BotCommands
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.GREETINGS
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.HELP_KEYWORD
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.HELP_TEXT
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.COMMAND_KEYWORD_LIST
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.START_KEYWORD
import com.notificator.bot.application.service.telegram.components.Buttons
import com.notificator.bot.domain.Notification
import com.notificator.bot.domain.Status
import com.notificator.bot.domain.UserDetails
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDate
import java.time.LocalTime

interface BotCommandHandler {
    fun handle(update: Update, block: (sendMessage: SendMessage) -> Unit)
}

@Component
class BotCommandHandlerImpl(
    private val userDetailsPersistencePort: UserDetailsPersistencePort,
    private val notificationPersistencePort: NotificationPersistencePort
) : BotCommandHandler, BotCommands {

    override fun handle(update: Update, block: (sendMessage: SendMessage) -> Unit) {
        when {
            update.isCommandMessage() -> handleCommandMessage(update, block)
            update.isTextMessage() -> handleTextMessage(update, block)
        }
    }

    private fun Update.isCommandMessage() = hasMessage() && message.hasText() && message.text in COMMAND_KEYWORD_LIST

    private fun handleCommandMessage(update: Update, block: (sendMessage: SendMessage) -> Unit) {
        val responseText = when (update.message.text) {
            START_KEYWORD -> GREETINGS
            HELP_KEYWORD -> HELP_TEXT
            else -> "Please, enter the correct command"
        }

        block(SendMessage().apply {
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

    private fun handleTextMessage(update: Update, block: (sendMessage: SendMessage) -> Unit) {
        userDetailsPersistencePort.save(update.message.from.toDomain())

        notificationPersistencePort.save(
            Notification(
                status = Status.PENDING,
                text = update.message.text,
                date = LocalDate.now(),
                time = LocalTime.now()
            )
        )

        block(SendMessage().apply {
            chatId = update.message.chatId.toString()
            text = "user saved"
        })
    }

    private fun User.toDomain() = UserDetails(
        id = id,
        firstName = firstName,
        userName = userName
    )
}
