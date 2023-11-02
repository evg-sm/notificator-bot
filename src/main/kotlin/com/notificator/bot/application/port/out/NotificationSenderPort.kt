package com.notificator.bot.application.port.out

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard

interface NotificationSenderPort {

    fun sendScheduled()
    fun sendMessage(toChatId: String, messageText: String, keyboard: ReplyKeyboard? = null)
    fun sendMessageAsLink(toChatId: String, messageText: String)
    fun sendEditMessageReplyMarkup(toChatId: String, toMessageId: Int, keyboard: InlineKeyboardMarkup)
}