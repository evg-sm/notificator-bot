package com.notificator.bot.adapter.telegram.components

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand


interface BotCommands {

    companion object {
        const val START_KEYWORD = "/start"
        const val HELP_KEYWORD = "/help"
        const val LIST_KEYWORD = "/list"
        const val CANCEL_KEYWORD = "/cancel"
        const val EDIT_KEYWORD = "/edit"

        val COMMAND_KEYWORD_LIST = listOf(START_KEYWORD, HELP_KEYWORD, LIST_KEYWORD, CANCEL_KEYWORD, EDIT_KEYWORD)

        val LIST_OF_COMMANDS = listOf(
            BotCommand(START_KEYWORD, "Создать уведомление"),
            BotCommand(EDIT_KEYWORD, "Редактировать уведомления"),
            BotCommand(LIST_KEYWORD, "Список уведомлений"),
            BotCommand(HELP_KEYWORD, "Помощь"),
            BotCommand(CANCEL_KEYWORD, "Отмена")
        )

        const val ASK_FOR_NOTIFICATION_TEXT = "О чем Вам напомнить?"

        val HELP_TEXT =
            """
            Приветствую! 
            Бот предназначен для формирования и отправки уведомлений.
            
            По команде /start можно сформировать новое уведомление.
            Уведмоления могут быть как единоразовыми, так и регулярными, например уведомления о Дне Рождения.
            Для формирования нового уведомления необходимо задать текст уведомления.
            Затем указать тип уведомления - 'единоразовое', 'каждый день', 'каждую неделю', 'каждый месяц', 'каждый год'.
            Указать дату уведомления.
            Указать время уведомления.
            
            При наступлении времени уведомления в чат бота будет отправлено текстовое сообщение.
            
            По команде /list можно получить список всех уведомлений пользователя.
            
            По команде /edit будет выслана ссылка на web интерфейс, где можно редактировать уведомления.
            
            Команда /cancel - отмена операции.
            
            """.trimIndent()
    }
}
