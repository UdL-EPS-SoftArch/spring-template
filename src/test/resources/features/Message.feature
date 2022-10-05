Feature: Message
  In order to use the app
  As a user
  I must be able to send and receive messages





  Scenario: Send message
    Given I create message with id "1000"
    When I send the message at when "20-04-2001 18:54" and the text "Hello"
    And send message an another user
    Then The response code is 201



