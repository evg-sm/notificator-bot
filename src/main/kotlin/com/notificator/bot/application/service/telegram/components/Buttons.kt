package com.notificator.bot.application.service.telegram.components

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


class Buttons {

    companion object {

        const val ONCE_KEYWORD = "#ONCE"
        const val REGULAR_KEYWORD = "#REGULAR"
        private val ONCE_TYPE_BUTTON = InlineKeyboardButton("Единоразовое").apply { callbackData = ONCE_KEYWORD }
        private val REGULAR_TYPE_BUTTON = InlineKeyboardButton("Регулярное").apply { callbackData = REGULAR_KEYWORD }

        /**
         * buttons for response message
         */
        fun notificationTypeButtons(): InlineKeyboardMarkup {
            val rowInline = listOf(ONCE_TYPE_BUTTON, REGULAR_TYPE_BUTTON)
            val rowsInLine = listOf(rowInline)
            return InlineKeyboardMarkup().apply { keyboard = rowsInLine }
        }
    }
}
