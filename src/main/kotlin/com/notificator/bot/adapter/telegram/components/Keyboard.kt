package com.notificator.bot.adapter.telegram.components

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


@Component
class Keyboard {

    companion object {
        const val ONCE_KEYWORD = "#ONCE"
        const val REGULAR_KEYWORD = "#REGULAR"
        private val ONCE_TYPE_BUTTON = InlineKeyboardButton("Единоразовое").apply { callbackData = ONCE_KEYWORD }
        private val REGULAR_TYPE_BUTTON = InlineKeyboardButton("Регулярное").apply { callbackData = REGULAR_KEYWORD }
    }

    fun notificationTypeInlineKeyboard(): InlineKeyboardMarkup {
        val rowInline = listOf(ONCE_TYPE_BUTTON, REGULAR_TYPE_BUTTON)
        val rowsInLine = listOf(rowInline)
        return InlineKeyboardMarkup().apply { keyboard = rowsInLine }
    }
}
