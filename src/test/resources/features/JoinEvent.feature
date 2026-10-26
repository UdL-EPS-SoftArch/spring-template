Feature: Join event
  In order to attend activities
  As a registered Erasmus student
  I want to join events (RSVP)

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: A student joins an event with available places
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And the event titled "Welcome picnic" has available places
    And I login as "luca" with password "password"
    When I join the event titled "Welcome picnic"
    Then The response code is 204
    And "luca" is registered for the event titled "Welcome picnic"

  Scenario: A student joining the same event twice is registered only once
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And "luca" is already registered for the event titled "Welcome picnic"
    And I login as "luca" with password "password"
    When I join the event titled "Welcome picnic"
    Then The response code is 204
    And "luca" is registered only once for the event titled "Welcome picnic"

  Scenario: A student cannot join an event that is full
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" with maximum 1 participant exists and is owned by "amina"
    And "amina" is registered for the event titled "Welcome picnic"
    And I login as "luca" with password "password"
    When I join the event titled "Welcome picnic"
    Then The response code is 409
    And "luca" is not registered for the event titled "Welcome picnic"

  Scenario: An unauthenticated visitor cannot join an event
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I join the event titled "Welcome picnic"
    Then The response code is 401

  Scenario: Two students compete for the last place and only one gets it
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And There is a registered user with username "sam" and password "password" and email "sam@sample.app"
    And an event titled "Welcome picnic" with maximum 1 participant exists and is owned by "amina"
    When "luca" and "sam" try to join the event titled "Welcome picnic" at the same time
    Then the two join attempts are answered with 201 and 409
    And the event titled "Welcome picnic" has 1 participant in the database

  Scenario: A student cannot join an event that does not exist
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I join an event that does not exist
    Then The response code is 404
