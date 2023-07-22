
Feature: User web ui test

  Scenario: Should return 'Home Page'
    When User request home page
    Then User check response status code 200
    And User check response body from file /ui/fixture/homePage.xml