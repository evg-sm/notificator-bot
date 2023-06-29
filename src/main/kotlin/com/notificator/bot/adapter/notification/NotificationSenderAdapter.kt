package com.notificator.bot.adapter.notification

import com.notificator.bot.adapter.telegram.NotificatorBot
import com.notificator.bot.application.port.out.NotificationStoragePort
import com.notificator.bot.application.port.out.NotificationSenderPort
import com.notificator.bot.domain.NotificationSendStatus
import com.notificator.bot.domain.NotificationType
import mu.KLogging
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.MessageEntity
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard

@Component
class NotificationSenderAdapter(
    private val persistencePort: NotificationStoragePort,
    @Lazy
    private val notificatorBot: NotificatorBot
) : NotificationSenderPort {

    companion object : KLogging()

    @Scheduled(cron = "0 */1 * * * *")
    fun sendScheduled() {
        persistencePort.selectUnsent().forEach { notification ->
            notificatorBot.execute(
                SendMessage().apply {
                    chatId = notification.chatId
                    text = notification.text
                }
            ).also {
                logger.info { "Send notification to user ${notification.userId}" }
            }

            when (notification.type) {
                NotificationType.ONCE -> persistencePort.save(notification.copy(sendStatus = NotificationSendStatus.SENT))

                NotificationType.EVERY_DAY ->
                    persistencePort.save(notification.copy(sendTime = notification.sendTime.plusDays(1L)))

                NotificationType.EVERY_WEEK ->
                    persistencePort.save(notification.copy(sendTime = notification.sendTime.plusWeeks(1L)))

                NotificationType.EVERY_MONTH ->
                    persistencePort.save(notification.copy(sendTime = notification.sendTime.plusMonths(1L)))

                NotificationType.EVERY_YEAR ->
                    persistencePort.save(notification.copy(sendTime = notification.sendTime.plusYears(1L)))

                NotificationType.UNDEFINED -> Unit
            }
        }
    }

    override fun sendMessage(toChatId: String, messageText: String, keyboard: ReplyKeyboard?) {
        notificatorBot.execute(
            SendMessage().apply {
                chatId = toChatId
                text = messageText
                replyMarkup = keyboard
                entities = listOf(
                    MessageEntity().apply {
                        offset = 0
                        length = messageText.length
                        type = "bold"
                    })
            }
        )
    }

    override fun sendMessageAsLink(toChatId: String, messageText: String) {
        notificatorBot.execute(
            SendMessage().apply {
                chatId = toChatId
                text = "<a href='$messageText'>Редактирование уведомлений</a>"
                parseMode = "HTML"
            }
        )
    }

    override fun sendEditMessageReplyMarkup(toChatId: String, toMessageId: Int, keyboard: InlineKeyboardMarkup) {
        notificatorBot.execute(
            EditMessageReplyMarkup().apply {
                chatId = toChatId
                messageId = toMessageId
                replyMarkup = keyboard
            })

    }
}
