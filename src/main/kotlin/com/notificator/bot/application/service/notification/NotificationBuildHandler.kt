package com.notificator.bot.application.service.notification

import com.notificator.bot.application.port.out.NotificationDraftStoragePort
import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.port.out.UserDetailsPersistencePort
import com.notificator.bot.application.service.telegram.components.Buttons
import com.notificator.bot.application.service.telegram.components.Buttons.Companion.ONCE_KEYWORD
import com.notificator.bot.application.service.telegram.components.Buttons.Companion.REGULAR_KEYWORD
import com.notificator.bot.domain.DraftState
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.NotificationType
import com.notificator.bot.domain.UserDetails
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
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
    private val notificationPersistencePort: NotificationPersistencePort
) : NotificationBuildHandler {

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
                    text = update.message.text,
                )
            )

            execute(SendMessage().apply {
                chatId = update.message.chatId.toString()
                text = "Уведомдение единоразовое или регулярное?"
                replyMarkup = Buttons.notificationTypeButtons()
            })

        } else {

            if (notificationDraft.draftState == DraftState.INIT) {
                notificationDraftStoragePort.get(userId)?.let { ntf ->

                    if (callbackData == null) {
                        execute(SendMessage().apply {
                            chatId = update.message.chatId.toString()
                            text = "Пожалуйста, укажите тип уведомдения единоразовое или регулярное?"
                            replyMarkup = Buttons.notificationTypeButtons()
                        })
                    } else {

                        val newNotificationType = when (callbackData) {
                            ONCE_KEYWORD -> NotificationType.ONCE
                            REGULAR_KEYWORD -> NotificationType.REGULAR
                            else -> NotificationType.UNDEFINED
                        }

                        notificationDraftStoragePort.set(
                            userId,
                            ntf.copy(type = newNotificationType, draftState = DraftState.TYPE_SET)
                        )

                        execute(SendMessage().apply {
                            chatId = notificationDraft.chatId
                            text = "Введите дату в формате 'dd.mm.YYYY', например: 12.01.2024"
                        })
                    }
                }
            }

            if (notificationDraft.draftState == DraftState.TYPE_SET) {
                notificationDraftStoragePort.get(userId)?.let { ntf ->
                    runCatching {
                        LocalDate.parse(update.message.text, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    }.onFailure {
                        execute(SendMessage().apply {
                            chatId = notificationDraft.chatId
                            text = "Пожалуйста, укажите дату в формате 'dd.mm.YYYY', например: 12.01.2024"
                        })
                    }.onSuccess { newDate: LocalDate ->
                        notificationDraftStoragePort.set(
                            userId,
                            ntf.copy(date = newDate, draftState = DraftState.DATE_SET)
                        )

                        execute(SendMessage().apply {
                            chatId = notificationDraft.chatId
                            text = "Введите время в формате 'HH:mm', например: '10:12'"
                        })
                    }
                }
            }

            if (notificationDraft.draftState == DraftState.DATE_SET) {
                notificationDraftStoragePort.get(update.message.from.id)?.let { ntf ->

                    runCatching {
                        LocalTime.parse(update.message.text, DateTimeFormatter.ofPattern("HH:mm"))
                    }.onFailure {
                        execute(SendMessage().apply {
                            chatId = update.message.chatId.toString()
                            text = "Пожалуйста, укажите время в формате 'HH:mm', например: '10:12'"
                        })
                    }.onSuccess { newTime: LocalTime ->
                        val finalNotification = ntf.copy(time = newTime, draftState = DraftState.TIME_SET)
                        notificationDraftStoragePort.clear(userId)
                        notificationPersistencePort.save(finalNotification)

                        execute(SendMessage().apply {
                            chatId = update.message.chatId.toString()
                            text = "Уведомление сохранено успешно!"
                        })
                    }
                }
            }
        }
    }

    private fun User.toDomain() = UserDetails(
        id = id,
        firstName = firstName,
        userName = userName
    )
}
