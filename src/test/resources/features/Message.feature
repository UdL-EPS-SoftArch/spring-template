Feature: Message
  In order to use the app
  As a user
  I must be able to send and receive messages

  Scenario: Send message
    Given The message with id 1 doesn't exist
    When I send the message whit id 1 and text "Hello"




