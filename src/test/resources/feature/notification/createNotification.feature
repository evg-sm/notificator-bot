
Feature: Create user notification

  @CleanNotificationTable
  Scenario: Create ONCE notification
    When User write notification text "once notification"
    And User write notification type "ONCE"
    And User write notification date "31.08.2035"
    And User write notification time "11:11"
    Then User check that notification was created in DB

  @CleanNotificationTable
  Scenario: Create EVERY_DAY notification
    When User write notification text "every day notification"
    And User write notification type "EVERY_DAY"
    And User write notification date "31.08.2035"
    And User write notification time "11:11"
    Then User check that notification was created in DB

  @CleanNotificationTable
  Scenario: Create EVERY_WEEK notification
    When User write notification text "every week notification"
    And User write notification type "EVERY_WEEK"
    And User write notification date "31.08.2035"
    And User write notification time "11:11"
    Then User check that notification was created in DB

  @CleanNotificationTable
  Scenario: Create EVERY_MONTH notification
    When User write notification text "every month notification"
    And User write notification type "EVERY_MONTH"
    And User write notification date "31.08.2035"
    And User write notification time "11:11"
    Then User check that notification was created in DB

  @CleanNotificationTable
  Scenario: Create EVERY_YEAR notification
    When User write notification text "every year notification"
    And User write notification type "EVERY_YEAR"
    And User write notification date "31.08.2035"
    And User write notification time "11:11"
    Then User check that notification was created in DB