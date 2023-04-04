package com.notificator.bot.application.service.telegram

import com.notificator.bot.application.port.`in`.TelegramListener
import com.notificator.bot.application.service.telegram.components.BotCommands
import com.notificator.bot.application.service.telegram.components.BotCommands.Companion.LIST_OF_COMMANDS
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import javax.annotation.PostConstruct

@Component
class NotificatorBot(
    @Value("\${app.telegram.token}") private val telegramToken: String,
    @Value("\${app.telegram.bot-username}") private val botUsername: String,
    private val botCommandHandler: BotCommandHandler
) : TelegramLongPollingBot(telegramToken), BotCommands, TelegramListener {

    override fun getBotUsername(): String = botUsername

    /**
     * setup left menu buttons
     */
    @PostConstruct
    fun init() {
        execute(SetMyCommands(LIST_OF_COMMANDS, BotCommandScopeDefault(), null))
    }

    override fun onUpdateReceived(update: Update) = botCommandHandler.handle(update, ::execute)

}