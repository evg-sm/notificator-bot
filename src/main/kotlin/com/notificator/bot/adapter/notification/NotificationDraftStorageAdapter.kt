package com.notificator.bot.adapter.notification

import com.notificator.bot.application.port.out.NotificationDraftStoragePort
import com.notificator.bot.domain.NotificationDraft
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class NotificationDraftStorageAdapter(
    @Value("\${app.storage.ttl}") private val storageTtl: Long
) : NotificationDraftStoragePort {

    private val storage = ConcurrentHashMap<Long, TtlWrapper>()

    override fun get(userId: Long): NotificationDraft? = storage[userId]?.notificationDraft

    override fun set(userId: Long, notificationDraft: NotificationDraft) {
        storage[userId] = TtlWrapper(
            createdMillis = System.currentTimeMillis(),
            notificationDraft = notificationDraft
        )
    }

    override fun removeByUserId(userId: Long) {
        storage.remove(userId)
    }

    @Scheduled(cron = "0 */3 * * * *")
    fun clearStorage() {
        storage.entries.forEach { entry ->
            val currentTimeMillis = System.currentTimeMillis()
            if ((currentTimeMillis - entry.value.createdMillis) >= storageTtl) {
                storage.remove(entry.key)
            }
        }
    }

    private data class TtlWrapper(
        val createdMillis: Long,
        val notificationDraft: NotificationDraft
    )
}
