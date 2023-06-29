package com.notificator.bot.adapter.notification

import com.github.benmanes.caffeine.cache.Cache
import com.notificator.bot.application.port.out.DraftNotificationStoragePort
import com.notificator.bot.domain.NotificationDraft
import org.springframework.stereotype.Component

@Component
class DraftNotificationStorageAdapter(
    private val caffeineCache: Cache<Long, NotificationDraft>
) : DraftNotificationStoragePort {

    override fun get(userId: Long): NotificationDraft? = caffeineCache.getIfPresent(userId)

    override fun set(userId: Long, notificationDraft: NotificationDraft) = caffeineCache.put(userId, notificationDraft)

    override fun invalidateByUserId(userId: Long) = caffeineCache.invalidate(userId)
}
