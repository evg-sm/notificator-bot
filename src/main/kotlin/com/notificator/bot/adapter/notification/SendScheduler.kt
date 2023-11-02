package com.notificator.bot.adapter.notification

import com.notificator.bot.application.port.out.NotificationSenderPort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SendScheduler(private val notificationSenderPort: NotificationSenderPort) {

    @Scheduled(cron = "0 */1 * * * *")
    fun sendScheduled() {
        notificationSenderPort.sendScheduled()
    }
}