Feature: Scheduled sending of notification

  @CleanNotificationTable
  Scenario: Once notification. Notification should change status to 'SEND' after sending
    Given User insert notification into DB with type "ONCE" and text "once"
    Then User check that after sending notification with text "once" change status to SENT

  @CleanNotificationTable
  Scenario: Every day notification. Notification should change 'send_time' to next day after sending
    Given User insert notification into DB with type "EVERY_DAY" and text "every day"
    Then User check that after sending notification with text "every day" change 'send_time' to next day

  @CleanNotificationTable
  Scenario: Every month notification. Notification should change 'send_time' to next month after sending
    Given User insert notification into DB with type "EVERY_MONTH" and text "every month"
    Then User check that after sending notification with text "every month" change 'send_time' to next month

  @CleanNotificationTable
  Scenario: Every year notification. Notification should change 'send_time' to next month after sending
    Given User insert notification into DB with type "EVERY_YEAR" and text "every year"
    Then User check that after sending notification with text "every year" change 'send_time' to next year
