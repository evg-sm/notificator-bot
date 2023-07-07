package com.notificator.bot.adapter.telegram.components

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


@Component
class Keyboard {

    companion object {
        const val ONCE_KEYWORD = "#ONCE"
        const val EVERY_DAY_KEYWORD = "#EVERY_DAY"
        const val EVERY_WEEK_KEYWORD = "#EVERY_WEEK"
        const val EVERY_MONTH_KEYWORD = "#EVERY_MONTH"
        const val EVERY_YEAR_KEYWORD = "#EVERY_YEAR"
        private val ONCE_BUTTON = InlineKeyboardButton("Одноразовое").apply { callbackData = ONCE_KEYWORD }
        private val EVERY_DAY_BUTTON = InlineKeyboardButton("Каждый день").apply { callbackData = EVERY_DAY_KEYWORD }
        private val EVERY_WEEK_BUTTON = InlineKeyboardButton("Каждую неделю").apply { callbackData = EVERY_WEEK_KEYWORD }
        private val EVERY_MONTH_BUTTON = InlineKeyboardButton("Каждый месяц").apply { callbackData = EVERY_MONTH_KEYWORD }
        private val EVERY_YEAR_BUTTON = InlineKeyboardButton("Каждый год").apply { callbackData = EVERY_YEAR_KEYWORD }
    }

    fun notificationTypeInlineKeyboard(): InlineKeyboardMarkup {
        val rowInline1 = listOf(ONCE_BUTTON, EVERY_DAY_BUTTON)
        val rowInline2 = listOf(EVERY_WEEK_BUTTON, EVERY_MONTH_BUTTON)
        val rowInline3 = listOf(EVERY_YEAR_BUTTON)
        val rowsInLine = listOf(rowInline1, rowInline2, rowInline3)
        return InlineKeyboardMarkup().apply { keyboard = rowsInLine }
    }
}
