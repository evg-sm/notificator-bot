package com.notificator.bot.application.port.out

import com.notificator.bot.domain.NotificationDraft

interface DraftNotificationStoragePort {

    fun get(userId: Long): NotificationDraft?

    fun set(userId: Long, notificationDraft: NotificationDraft)

    fun invalidateByUserId(userId: Long)
}
