package com.notificator.bot.storage

import com.notificator.bot.adapter.notification.NotificationDraftStorageAdapter
import com.notificator.bot.domain.DraftState
import com.notificator.bot.domain.NotificationDraft
import com.notificator.bot.domain.NotificationType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class NotificationDraftStorageAdapterUnitTest {

    @Test
    fun `should clear storage`() {
        val userId = 1L
        val ttl = 1L
        val storage = NotificationDraftStorageAdapter(ttl)

        val notificationDraft = NotificationDraft(
            userId = userId,
            chatId = "123",
            draftState = DraftState.INIT,
            type = NotificationType.UNDEFINED,
            text = "some text"
        )

        storage.set(userId, notificationDraft)

        storage.get(userId) shouldBe notificationDraft

        storage.clearStorage()

        storage.get(userId) shouldBe null
    }
}