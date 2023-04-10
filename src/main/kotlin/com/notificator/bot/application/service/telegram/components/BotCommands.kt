package com.notificator.bot.application.service.telegram.components

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand


interface BotCommands {

    companion object {
        const val START_KEYWORD = "/start"
        const val HELP_KEYWORD = "/help"
        const val LIST_KEYWORD = "/list"
        const val CANCEL_KEYWORD = "/cancel"

        val COMMAND_KEYWORD_LIST = listOf(START_KEYWORD, HELP_KEYWORD, LIST_KEYWORD, CANCEL_KEYWORD)

        val LIST_OF_COMMANDS = listOf(
            BotCommand(START_KEYWORD, "start bot"),
            BotCommand(HELP_KEYWORD, "bot help"),
            BotCommand(LIST_KEYWORD, "list of users notifications"),
            BotCommand(CANCEL_KEYWORD, "cancel")
        )

        const val ASK_FOR_NOTIFICATION_TEXT = "О чем Вам напомнить?"

        val HELP_TEXT =
            """
            Приветствую! 
            Бот предназначен для формирования и отправки уведомлений.
            
            По команде /start можно сформировать новое уведомление.
            Уведмоления могут быть как единоразовыми, так и регулярными, например уведомления о Дне Рождения.
            Для формирования нового уведомления необходимо задать текст уведомления.
            Затем указать тип уведомления - 'единоразовое' или 'регулярное'
            Указать дату уведомления.
            Указать время уведомления.
            
            При наступлении времени уведомления в чат бота будет отправлено текстовое сообщение.
            
            По команде /list можно получить список всех уведомлений пользователя.
            """.trimIndent()
    }
}
