
Feature: User web ui test

  @CleanNotificationTable
  Scenario: Should return 'Home Page'
    When User request home page
    Then User check response status code 200
    And User check response body from file /ui/fixture/homePage.xml

  @CleanNotificationTable
  Scenario: Should return notification list page with one row
    Given User insert notification into DB
    When User request notification list page
    Then User check response status code 200
    And User check response body from file /ui/fixture/notificationListPage.xml