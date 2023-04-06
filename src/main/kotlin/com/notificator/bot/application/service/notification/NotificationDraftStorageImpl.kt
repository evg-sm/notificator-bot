package com.notificator.bot.application.service.notification

import com.notificator.bot.application.port.out.NotificationDraftStorage
import com.notificator.bot.domain.NotificationDraft
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class NotificationDraftStorageImpl: NotificationDraftStorage {

    private val storage = ConcurrentHashMap<Long, NotificationDraft>()

    override fun get(userId: Long): NotificationDraft? = storage[userId]

    override fun set(userId: Long, notificationDraft: NotificationDraft) {
        storage[userId] = notificationDraft
    }

    override fun delete(userId: Long) {
        storage.remove(userId)
    }
}
