package com.notificator.bot

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = [PostgreSQLContainerInitializer::class])
class BaseContextTest {

    @Test
    fun contextLoads() {
    }

}
