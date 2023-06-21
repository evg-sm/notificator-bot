package com.notificator.bot.adapter.telegram.components

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@Component
class CalendarKeyboard {

    companion object {
        const val IGNORE = "ignore!@#$%^&"
        val WEEK_DAYS = arrayOf("П", "В", "С", "Ч", "П", "С", "В")
        val FORWARD_CALLBACK = ">"
        val BACKWARD_CALLBACK = "<"
    }

    fun inlineKeyboard(date: LocalDate): InlineKeyboardMarkup {
        val calendarKeyboard: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        // row - Month and Year
        val headerRow: MutableList<InlineKeyboardButton> = mutableListOf()
        headerRow.add(
            createButton(
                IGNORE,
                date.format(DateTimeFormatter.ofPattern("MMMM yyyy").localizedBy(Locale.forLanguageTag("ru")))
            )
        )
        calendarKeyboard.add(headerRow)

        // row - Days of the week
        val daysOfWeekRow: MutableList<InlineKeyboardButton> = mutableListOf()
        for (day in WEEK_DAYS) {
            daysOfWeekRow.add(createButton(IGNORE, day))
        }
        calendarKeyboard.add(daysOfWeekRow)

        var firstDay = date.withDayOfMonth(1)
        var shift = firstDay.dayOfWeek.value - 1
        val daysInMonth = firstDay.month.length(false)
        val rows = (if ((daysInMonth + shift) % 7 > 0) 1 else 0) + (daysInMonth + shift) / 7
        for (i in 0 until rows) {
            calendarKeyboard.add(buildRow(firstDay, shift))
            firstDay = firstDay.plusDays((7 - shift).toLong())
            shift = 0
        }
        val controlsRow: MutableList<InlineKeyboardButton> = ArrayList()
        controlsRow.add(createButton(BACKWARD_CALLBACK, BACKWARD_CALLBACK))
        controlsRow.add(createButton(FORWARD_CALLBACK, FORWARD_CALLBACK))
        calendarKeyboard.add(controlsRow)

        return InlineKeyboardMarkup().apply {
            keyboard = calendarKeyboard
        }
    }

    private fun createButton(callBack: String, text: String) =
        InlineKeyboardButton(text).apply { callbackData = callBack }

    private fun buildRow(date: LocalDate, shift: Int): MutableList<InlineKeyboardButton> {
        val row: MutableList<InlineKeyboardButton> = ArrayList()
        var day = date.dayOfMonth
        var callbackDate = date
        for (j in 0 until shift) {
            row.add(createButton(IGNORE, " "))
        }
        for (j in shift..6) {
            if (day <= date.month.length(false)) {
                row.add(createButton(callbackDate.formatAsPattern(), (day++).toString()))
                callbackDate = callbackDate.plusDays(1)
            } else {
                row.add(createButton(IGNORE, " "))
            }
        }
        return row
    }

    private fun LocalDate.formatAsPattern() = format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}