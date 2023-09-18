package com.notificator.bot.adapter.telegram

import com.notificator.bot.adapter.telegram.components.BotCommands
import com.notificator.bot.adapter.telegram.components.BotCommands.Companion.LIST_OF_COMMANDS
import com.notificator.bot.application.configuration.BotSetting
import com.notificator.bot.application.port.`in`.NotificationListener
import mu.KLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import javax.annotation.PostConstruct

@Component
class NotificatorBot(
    private val botSetting: BotSetting,
    private val botCommandHandler: BotCommandHandler,
    private val notificationBuildHandler: NotificationBuildHandler
) : TelegramLongPollingBot(botSetting.token), BotCommands, NotificationListener {

    companion object : KLogging()

    override fun getBotUsername(): String = botSetting.botUsername

    /**
     * setup chat left menu buttons
     */
    @PostConstruct
    fun init() {
        execute(SetMyCommands(LIST_OF_COMMANDS, BotCommandScopeDefault(), null))
    }

    override fun onUpdateReceived(update: Update) {
        update.logIncomingMessage()
        when {
            update.isCommandMessage() -> botCommandHandler.handle(update)
            else -> notificationBuildHandler.handle(update, ::execute)
        }
    }

    private fun Update.logIncomingMessage() {
        message?.text?.let {
            logger.info { "Received text message ${String(it.toByteArray(), Charsets.UTF_8)}" }
        }
        callbackQuery?.data?.let {
            logger.info { "Received callback data ${callbackQuery.data}" }
        }
    }

    private fun Update.isCommandMessage() =
        hasMessage() && message.hasText() && message.text in BotCommands.COMMAND_KEYWORD_LIST
}