package com.notificator.bot.cucumber.step

import com.notificator.bot.adapter.telegram.NotificatorBot
import com.notificator.bot.cucumber.helper.DbHelper
import com.notificator.bot.default_chat_id
import com.notificator.bot.default_user_id
import com.notificator.bot.domain.NotificationSendStatus
import io.cucumber.java8.En
import io.kotest.matchers.shouldBe
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.testcontainers.shaded.org.awaitility.Awaitility.await
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class NotificationStepDefinitions(
    private val notificatorBot: NotificatorBot,
    private val dbHelper: DbHelper
) : En {

    companion object {
        private val EXPECTED_NOTIFICATION_STATUS = NotificationSendStatus.PENDING.name
        private const val EXPECTED_DATE_TIME = "2035-08-31 11:11:00.0"
    }

    init {

        Given("User insert notification into DB") {
            dbHelper.insert(
                """
                insert into bot.notification (user_id, chat_id, type, send_status, text, send_time)
                values ($default_user_id, $default_chat_id, 'EVERY_YEAR', 'PENDING', 'TEST', '2035-08-31 11:11:00')
                """
            )
        }

        Given("User insert notification into DB with type {string} and text {string}") { type: String, name: String ->
            ScenarioContext.notificationName.set(name)
            dbHelper.insert(
                """
                insert into bot.notification (user_id, chat_id, type, send_status, text, send_time)
                values ($default_user_id, $default_chat_id, '$type', 'PENDING', '$name', 
                '${LocalDate.now().format()} ${LocalTime.now().plusMinutes(1).format()}:00')
                """
            )
        }

        When("User write notification text {string}") { name: String ->
            ScenarioContext.notificationName.set(name)
            notificatorBot.onUpdateReceived(createTextUpdate(name))
        }

        When("User write notification type {string}") { type: String ->
            ScenarioContext.notificationType.set(type)
            notificatorBot.onUpdateReceived(createCallbackUpdate("#$type"))
        }

        When("User write notification date {string}") { date: String ->
            notificatorBot.onUpdateReceived(createCallbackUpdate(date))
        }

        When("User write notification time {string}") { time: String ->
            notificatorBot.onUpdateReceived(createTextUpdate(time))
        }

        Then("User check that notification was created in DB") {
            val rowAsMap = selectNotificationByText(ScenarioContext.notificationName.get())

            rowAsMap["text"] shouldBe ScenarioContext.notificationName.get()
            rowAsMap["type"] shouldBe ScenarioContext.notificationType.get()
            rowAsMap["send_status"] shouldBe EXPECTED_NOTIFICATION_STATUS
            rowAsMap["send_time"].toString() shouldBe EXPECTED_DATE_TIME
        }

        Then("User check that after sending notification with text {string} change status to SENT") { text: String ->
            awaitUntil { selectNotificationByText(text)["send_status"]?.equals(NotificationSendStatus.SENT.name) ?: false }

            selectNotificationByText(text)["send_status"] shouldBe NotificationSendStatus.SENT.name
        }

        Then("User check that after sending notification with text {string} change 'send_time' to next day") { text: String ->
            awaitUntil { (selectNotificationByText(text)["send_time"] as Timestamp).toLocalDateTime().toLocalDate().equals(LocalDate.now().plusDays(1)) }

            val rowAsMap = selectNotificationByText(text)
            (rowAsMap["send_time"] as Timestamp).toLocalDateTime().toLocalDate() shouldBe LocalDate.now().plusDays(1)
            rowAsMap["send_status"] shouldBe NotificationSendStatus.PENDING.name
        }

        Then("User check that after sending notification with text {string} change 'send_time' to next month") { text: String ->
            awaitUntil { (selectNotificationByText(text)["send_time"] as Timestamp).toLocalDateTime().toLocalDate().equals(LocalDate.now().plusMonths(1)) }

            val rowAsMap = selectNotificationByText(text)
            (rowAsMap["send_time"] as Timestamp).toLocalDateTime().toLocalDate() shouldBe LocalDate.now().plusMonths(1)
            rowAsMap["send_status"] shouldBe NotificationSendStatus.PENDING.name
        }

        Then("User check that after sending notification with text {string} change 'send_time' to next year") { text: String ->
            awaitUntil { (selectNotificationByText(text)["send_time"] as Timestamp).toLocalDateTime().toLocalDate().equals(LocalDate.now().plusYears(1)) }

            val rowAsMap = selectNotificationByText(text)
            (rowAsMap["send_time"] as Timestamp).toLocalDateTime().toLocalDate() shouldBe LocalDate.now().plusYears(1)
            rowAsMap["send_status"] shouldBe NotificationSendStatus.PENDING.name
        }
    }

    private fun selectNotificationByText(text: String): MutableMap<String, Any> = dbHelper.queryForMap(
        """select * from bot.notification where text = '$text'"""
    )

    private fun awaitUntil(until: () -> Boolean) {
        await()
            .atLeast(Duration.ofMillis(1000L))
            .atMost(Duration.ofMinutes(2L))
            .with()
            .pollInterval(Duration.ofMillis(500L))
            .until { until() }
    }

    private fun LocalDate.format(): String = format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    private fun LocalTime.format(): String = format(DateTimeFormatter.ofPattern("HH:mm"))

    private fun createTextUpdate(messageText: String): Update = Update().apply {
        updateId = Random().nextInt()
        message = Message().apply {
            messageId = Random().nextInt()
            from = buildUser()
            chat = buildChat()
            text = messageText
        }
    }

    private fun createCallbackUpdate(callbackData: String): Update = Update().apply {
        updateId = Random().nextInt()
        callbackQuery = CallbackQuery().apply {
            id = Random().nextInt().toString()
            from = buildUser()
            message = Message().apply {
                messageId = Random().nextInt()
                from = buildUser()
                chat = buildChat()
            }
            data = callbackData
        }
    }

    private fun buildUser(): User = User().apply {
        id = 236637434
        firstName = "firstName"
        isBot = false
        lastName = "lastName"
        languageCode = "ru"
    }

    private fun buildChat(): Chat = Chat().apply {
        id = 236637434
        type = "private"
        firstName = "firstName"
        lastName = "lastName"
        userName = "userName"
    }
}
