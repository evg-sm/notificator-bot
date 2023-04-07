package com.notificator.bot.application.port.out

import com.notificator.bot.domain.NotificationDraft

interface NotificationDraftStoragePort {

    fun get(userId: Long): NotificationDraft?

    fun set(userId: Long, notificationDraft: NotificationDraft)

    fun delete(userId: Long)
}
