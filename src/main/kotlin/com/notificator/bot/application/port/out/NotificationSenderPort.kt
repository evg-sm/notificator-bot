package com.notificator.bot.application.port.out

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard

interface NotificationSenderPort {
    fun sendMessage(toChatId: String, messageText: String, keyboard: ReplyKeyboard? = null)
    fun sendEditMessageReplyMarkup(toChatId: String, toMessageId: Int, keyboard: InlineKeyboardMarkup)
}