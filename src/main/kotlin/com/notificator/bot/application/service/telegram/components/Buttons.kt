package com.notificator.bot.application.service.telegram.components

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


class Buttons {

    companion object {

        private val START_BUTTON = InlineKeyboardButton("Start")
        private val HELP_BUTTON = InlineKeyboardButton("Help")

        /**
         * buttons for response
         */
        fun inlineMarkup(): InlineKeyboardMarkup {
            START_BUTTON.callbackData = "#collback_start_button"
            HELP_BUTTON.callbackData = "#collback_help_button"
            val rowInline = listOf(START_BUTTON, HELP_BUTTON)
            val rowsInLine = listOf(rowInline)
            val markupInline = InlineKeyboardMarkup()
            markupInline.keyboard = rowsInLine
            return markupInline
        }
    }
}
