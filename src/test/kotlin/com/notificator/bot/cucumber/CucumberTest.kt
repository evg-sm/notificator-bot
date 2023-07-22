package com.notificator.bot.cucumber

import com.notificator.bot.cucumber.configuration.CucumberTestConfiguration
import com.notificator.bot.initializer.PostgreSQLContainerInitializer
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListener
import org.springframework.test.context.TestExecutionListeners

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [PostgreSQLContainerInitializer::class])
@CucumberContextConfiguration
@TestExecutionListeners(
    listeners = [CucumberTest::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Import(CucumberTestConfiguration::class)
class CucumberTest : TestExecutionListener {
}