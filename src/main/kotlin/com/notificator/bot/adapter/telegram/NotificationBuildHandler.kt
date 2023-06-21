package com.notificator.bot.adapter.telegram

import com.notificator.bot.application.port.out.NotificationDraftStoragePort
import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.port.out.NotificationSenderPort
import com.notificator.bot.application.port.out.UserDetailsPersistencePort
import com.notificator.bot.adapter.telegram.components.CalendarKeyboard
import com.notificator.bot.adapter.telegram.components.CalendarKeyboard.Companion.BACKWARD_CALLBACK
import com.notificator.bot.adapter.telegram.components.CalendarKeyboard.Companion.FORWARD_CALLBACK
import com.notificator.bot.adapter.telegram.components.Keyboard
import com.notificator.bot.adapter.telegram.components.Keyboard.Companion.EVERY_DAY_KEYWORD
import com.notificator.bot.adapter.telegram.components.Keyboard.Companion.EVERY_MONTH_KEYWORD
import com.notificator.bot.adapter.telegram.components.Keyboard.Companion.EVERY_WEEK_KEYWORD
import com.notificator.bot.adapter.telegram.components.Keyboard.Companion.ONCE_KEYWORD
import com.notificator.bot.adapter.telegram.components.Keyboard.Companion.EVERY_YEAR_KEYWORD
import com.notificator.bot.domain.DraftState
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.NotificationType
import mu.KLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

interface NotificationBuildHandler {
    fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit)
}

@Component
class NotificationBuildHandlerImpl(
    private val notificationDraftStoragePort: NotificationDraftStoragePort,
    private val userDetailsPersistencePort: UserDetailsPersistencePort,
    private val notificationPersistencePort: NotificationPersistencePort,
    private val keyboard: Keyboard,
    private val calendarKeyboard: CalendarKeyboard,
    private val notificationSenderPort: NotificationSenderPort,
) : NotificationBuildHandler {

    companion object : KLogging()

    override fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        val userId: Long = update.callbackQuery?.from?.id ?: update.message.from.id
        val callbackData: String? = update.callbackQuery?.data
        val notificationDraft: NotificationDraft? = notificationDraftStoragePort.get(userId)

        if (notificationDraft == null) {
            notificationDraftStoragePort.set(
                userId,
                NotificationDraft(
                    userId = userId,
                    chatId = update.message.chatId.toString(),
                    draftState = DraftState.INIT,
                    type = NotificationType.UNDEFINED,
                    text = update.message.text
                )
            )
            notificationSenderPort.sendMessage(
                toChatId = update.message.chatId.toString(),
                messageText = "Укажите частоту уведомдения?",
                keyboard = keyboard.notificationTypeInlineKeyboard()
            )

        } else {

            if (notificationDraft.draftState == DraftState.INIT) {
                notificationDraftStoragePort.get(userId)?.let { ntf ->

                    if (callbackData == null) {
                        notificationSenderPort.sendMessage(
                            toChatId = update.message.chatId.toString(),
                            messageText = "Пожалуйста, укажите частоту уведомдения?",
                            keyboard = keyboard.notificationTypeInlineKeyboard()
                        )
                    } else {

                        val newNotificationType = when (callbackData) {
                            ONCE_KEYWORD -> NotificationType.ONCE
                            EVERY_DAY_KEYWORD -> NotificationType.EVERY_DAY
                            EVERY_WEEK_KEYWORD -> NotificationType.EVERY_WEEK
                            EVERY_MONTH_KEYWORD -> NotificationType.EVERY_MONTH
                            EVERY_YEAR_KEYWORD -> NotificationType.EVERY_YEAR
                            else -> NotificationType.UNDEFINED
                        }
                        notificationDraftStoragePort.set(
                            userId,
                            ntf.copy(type = newNotificationType, draftState = DraftState.TYPE_SET)
                        )
                        notificationSenderPort.sendMessage(
                            toChatId = notificationDraft.chatId,
                            messageText = "Выберите дату в календаре или введите дату в формате 'dd.mm.YYYY', например: 12.01.2024",
                            keyboard = calendarKeyboard.inlineKeyboard(LocalDate.now())
                        )
                    }
                }
            }

            if (notificationDraft.draftState == DraftState.TYPE_SET) {

                if (callbackData == null && update.message != null) {
                    notificationSenderPort.sendMessage(
                        toChatId = notificationDraft.chatId,
                        messageText = "Пожалуйста, выберите дату в календаре или введите дату в формате 'dd.mm.YYYY', например: 12.01.2024",
                        keyboard = calendarKeyboard.inlineKeyboard(LocalDate.now())
                    )
                    return
                }

                notificationDraftStoragePort.get(userId)?.let { ntf: NotificationDraft ->

                    val callbackQueryData = update.callbackQuery.data

                    if (callbackQueryData.contains(FORWARD_CALLBACK) || callbackQueryData.contains(BACKWARD_CALLBACK)) {

                        if (callbackQueryData.contains(FORWARD_CALLBACK)) {
                            ntf.monthCounter++
                        } else {
                            ntf.monthCounter--
                        }

                        logger.info { "callbackQuery.message.messageId=${update.callbackQuery.message.messageId}" }

                        notificationSenderPort.sendEditMessageReplyMarkup(
                            toChatId = notificationDraft.chatId,
                            toMessageId = update.callbackQuery.message.messageId,
                            keyboard = calendarKeyboard.inlineKeyboard(
                                LocalDate.now().plusMonths(ntf.monthCounter)
                            )
                        )
                        return
                    }

                    runCatching {
                        LocalDate.parse(callbackQueryData, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    }.onFailure {
                        notificationSenderPort.sendMessage(
                            toChatId = notificationDraft.chatId,
                            messageText = "Пожалуйста, выберите дату в календаре или введите дату в формате 'dd.mm.YYYY', например: 12.01.2024",
                            keyboard = calendarKeyboard.inlineKeyboard(LocalDate.now())
                        )
                    }.onSuccess { newDate: LocalDate ->

                        if (newDate < LocalDate.now()) {
                            notificationSenderPort.sendMessage(
                                toChatId = notificationDraft.chatId,
                                messageText = "Пожалуйста, укажите дату большую или равную текущей",
                                keyboard = calendarKeyboard.inlineKeyboard(LocalDate.now())
                            )

                        } else {

                            notificationDraftStoragePort.set(
                                userId,
                                ntf.copy(date = newDate, draftState = DraftState.DATE_SET)
                            )
                            notificationSenderPort.sendMessage(
                                toChatId = notificationDraft.chatId,
                                messageText = "Введите время в формате 'HH:mm', например: '10:12'"
                            )
                        }
                    }
                }
            }

            if (notificationDraft.draftState == DraftState.DATE_SET) {
                notificationDraftStoragePort.get(update.message.from.id)?.let { ntf ->

                    runCatching {
                        LocalTime.parse(update.message.text, DateTimeFormatter.ofPattern("HH:mm"))
                    }.onFailure {
                        notificationSenderPort.sendMessage(
                            toChatId = notificationDraft.chatId,
                            messageText = "Пожалуйста, укажите время в формате 'HH:mm', например: '10:12'"
                        )
                    }.onSuccess { newTime: LocalTime ->
                        val finalNotification = ntf.copy(time = newTime, draftState = DraftState.TIME_SET)
                        notificationDraftStoragePort.removeByUserId(userId)
                        notificationPersistencePort.save(finalNotification)
                        notificationSenderPort.sendMessage(
                            toChatId = notificationDraft.chatId,
                            messageText = "Уведомление сохранено успешно!"
                        )
                    }
                }
            }
        }
    }
}
