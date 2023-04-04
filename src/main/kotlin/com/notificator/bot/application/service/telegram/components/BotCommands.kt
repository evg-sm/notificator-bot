package com.notificator.bot.application.service.telegram.components

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand


interface BotCommands {

    companion object {
        const val START_KEYWORD = "/start"
        const val HELP_KEYWORD = "/help"

        val COMMAND_KEYWORD_LIST = listOf(START_KEYWORD, HELP_KEYWORD)

        val LIST_OF_COMMANDS = listOf(
            BotCommand(START_KEYWORD, "start bot"),
            BotCommand(HELP_KEYWORD, "bot help"),
            BotCommand("/new", "some new command")
        )

        val HELP_TEXT =
            """
            This bot will help to count the number of messages in the chat. The following commands are available to you:
            
            /start - start the bot
            /help - help menu
            """.trimIndent()

        val GREETINGS = "GOOD MORNING, EVERYONE !!!"
    }
}