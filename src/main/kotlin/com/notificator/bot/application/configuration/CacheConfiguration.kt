package com.notificator.bot.application.configuration

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.notificator.bot.domain.NotificationDraft
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CacheConfiguration(
    @Value("\${app.storage.ttl}")
    private val storageTtl: Long
) {

    @Bean
    fun caffeineCache(): Cache<Long, NotificationDraft> {
       return Caffeine.newBuilder().expireAfterWrite(Duration.ofMillis(storageTtl)).build()
    }
}
