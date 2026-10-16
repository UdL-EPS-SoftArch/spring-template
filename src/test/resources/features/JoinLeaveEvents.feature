Feature: Join and leave events
  In order to attend activities
  As a registered Erasmus student
  I want to join and leave events

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
    Then The response code is 201
    And "luca" is registered for the event titled "Welcome picnic"

  Scenario: A student cannot join the same event more than once
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And "luca" is already registered for the event titled "Welcome picnic"
    And I login as "luca" with password "password"
    When I join the event titled "Welcome picnic"
    Then The response code is 409
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

  Scenario Outline: An unauthenticated visitor cannot join or leave an event
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I <action> the event titled "Welcome picnic"
    Then The response code is 401

    Examples:
      | action |
      | join   |
      | leave  |

  Scenario: A student leaves an event they joined
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And "luca" is already registered for the event titled "Welcome picnic"
    And I login as "luca" with password "password"
    When I leave the event titled "Welcome picnic"
    Then The response code is 204
    And "luca" is not registered for the event titled "Welcome picnic"

  Scenario: A student cannot leave an event they did not join
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And "luca" is not registered for the event titled "Welcome picnic"
    And I login as "luca" with password "password"
    When I leave the event titled "Welcome picnic"
    Then The response code is 404