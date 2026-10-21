Feature: Redirect anonymous visitors to registration
  In order to join an event
  As an anonymous visitor
  I want to register or log in

  Scenario: An anonymous visitor is redirected to registration when trying to join an event
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is publicly available
    When I try to join the event titled "Welcome picnic"
    Then I am redirected to the registration page
    And I see a message asking me to register or log in
    And I am not registered for the event titled "Welcome picnic"