Feature: Event optimistic locking
  In order to avoid losing concurrent changes
  As an event owner using two clients
  I want a stale update to be rejected

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: A stale update of an event is rejected
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "amina" with password "password"
    And two clients have loaded the event titled "Welcome picnic"
    When client 1 updates the event address to "Riverside park"
    Then client 1 update succeeds
    When client 2 updates the event address to "Main square" using the initial version
    Then client 2 update is rejected with a client error
