package com.notificator.bot.application.service.notification

import com.notificator.bot.application.port.out.NotificationDraftStorage
import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.port.out.UserDetailsPersistencePort
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.State
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
    private val notificationDraftStorage: NotificationDraftStorage,
    private val userDetailsPersistencePort: UserDetailsPersistencePort,
    private val notificationPersistencePort: NotificationPersistencePort
) : NotificationBuildHandler {

    override fun handle(update: Update, execute: (sendMessage: SendMessage) -> Unit) {
        userDetailsPersistencePort.save(update.message.from.toDomain())

        val userId = update.message.from.id
        val messageText = update.message.text
        val notificationDraft: NotificationDraft? = notificationDraftStorage.get(userId)

        if (notificationDraft == null) {
            notificationDraftStorage.set(
                update.message.from.id,
                NotificationDraft(
                    userId = update.message.from.id,
                    state = State.INIT,
                    type = NotificationType.UNDEFINED,
                    text = update.message.text,
                )
            )

            execute(SendMessage().apply {
                chatId = update.message.chatId.toString()
                text = "Напоминание единоразовое или регулярное?"
            })
        }

        if (notificationDraft != null && notificationDraft.state == State.INIT) {
            notificationDraftStorage.get(userId)?.let { ntf ->
                val newNotificationType = when (messageText) {
                    "единоразовое" -> NotificationType.ONCE
                    "регулярное" -> NotificationType.REGULAR
                    else -> NotificationType.UNDEFINED
                }

                notificationDraftStorage.set(userId, ntf.copy(type = newNotificationType, state = State.TYPE_SET))

                execute(SendMessage().apply {
                    chatId = update.message.chatId.toString()
                    text = "Введите дату в формате 'dd.mm.YYYY'"
                })
            }
        }

        if (notificationDraft != null && notificationDraft.state == State.TYPE_SET) {
            notificationDraftStorage.get(userId)?.let { ntf ->
                val newDate = LocalDate.parse(messageText, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                notificationDraftStorage.set(userId, ntf.copy(date = newDate, state = State.DATE_SET))

                execute(SendMessage().apply {
                    chatId = update.message.chatId.toString()
                    text = "Введите время в формате '10:12'"
                })
            }
        }

        if (notificationDraft != null && notificationDraft.state == State.DATE_SET) {
            notificationDraftStorage.get(userId)?.let { ntf ->
                val newTime = LocalTime.parse(messageText, DateTimeFormatter.ofPattern("HH:mm"))

                val finalNotification = ntf.copy(time = newTime, state = State.TIME_SET)
                notificationDraftStorage.delete(userId)
                notificationPersistencePort.save(finalNotification)

                execute(SendMessage().apply {
                    chatId = update.message.chatId.toString()
                    text = "Уведомление сохранено успешно!"
                })


            }
        }
    }

    private fun User.toDomain() = UserDetails(
        id = id,
        firstName = firstName,
        userName = userName
    )
}
