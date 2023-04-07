package com.notificator.bot.application.service.notification

import com.notificator.bot.application.port.out.NotificationPersistencePort
import com.notificator.bot.application.port.out.NotificationSenderPort
import com.notificator.bot.application.service.telegram.NotificatorBot
import com.notificator.bot.domain.NotificationSendStatus
import com.notificator.bot.domain.NotificationType
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class NotificationSenderAdapter(
    private val persistencePort: NotificationPersistencePort,
    private val notificatorBot: NotificatorBot
) : NotificationSenderPort {

    companion object : KLogging()

    @Scheduled(cron = "0 */1 * * * *")
    override fun send() {
        logger.info { "Notification sender start" }

        persistencePort.selectUnsent().forEach { notification ->

            logger.info { "notification $notification" }

            notificatorBot.execute(
                SendMessage().apply {
                    chatId = notification.chatId
                    text = notification.text
                }
            )

            when (notification.type) {
                NotificationType.ONCE -> persistencePort.save(notification.copy(sendStatus = NotificationSendStatus.SENT))

                NotificationType.REGULAR ->
                    persistencePort.save(notification.copy(dateTime = notification.dateTime.plusYears(1L)))

                NotificationType.UNDEFINED -> Unit
            }
        }
    }
}
