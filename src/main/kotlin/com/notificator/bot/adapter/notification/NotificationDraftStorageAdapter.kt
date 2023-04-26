package com.notificator.bot.adapter.notification

import com.notificator.bot.application.port.out.NotificationDraftStoragePort
import com.notificator.bot.domain.NotificationDraft
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class NotificationDraftStorageAdapter: NotificationDraftStoragePort {

    private val storage = ConcurrentHashMap<Long, NotificationDraft>()

    override fun get(userId: Long): NotificationDraft? = storage[userId]

    override fun set(userId: Long, notificationDraft: NotificationDraft) {
        storage[userId] = notificationDraft
    }

    // todo make auto cleat by ttl
    override fun clear(userId: Long) {
        storage.remove(userId)
    }
}
