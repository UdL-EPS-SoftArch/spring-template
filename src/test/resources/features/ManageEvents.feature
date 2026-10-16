Feature: Manage events
  In order to keep event information up to date
  As an event owner
  I want to edit and delete my events

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: The owner updates their event
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "amina" with password "password"
    When I update the event titled "Welcome picnic" with:
      | title           | Erasmus welcome picnic              |
      | description     | Meet other Erasmus students         |
      | address         | Riverside park                      |
      | dateTime        | 2030-11-11T16:00:00                 |
      | maxParticipants | 25                                  |
      | estimatedCost   | 6.00                                |
      | subcategory     | Meetups                             |
    Then The response code is 200
    And the event titled "Erasmus welcome picnic" is owned by "amina"

  Scenario Outline: The owner cannot update an event without a required field
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "amina" with password "password"
    When I update the event titled "Welcome picnic" without "<field>"
    Then The response code is 400

    Examples:
      | field           |
      | title           |
      | description     |
      | address         |
      | dateTime        |
      | maxParticipants |
      | estimatedCost   |
      | subcategory     |

  Scenario: An unauthenticated visitor cannot update an event
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I update the event titled "Welcome picnic" with:
      | title | Updated picnic |
    Then The response code is 401

  Scenario: A student cannot update another student's event
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    When I update the event titled "Welcome picnic" with:
      | title | Updated picnic |
    Then The response code is 404

  Scenario: The owner cannot update an event that does not exist
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I update the event titled "Missing event" with:
      | title | Updated event |
    Then The response code is 404

  Scenario: The owner deletes their event
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "amina" with password "password"
    When I delete the event titled "Welcome picnic"
    Then The response code is 204
    And the event titled "Welcome picnic" does not exist

  Scenario: An unauthenticated visitor cannot delete an event
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I delete the event titled "Welcome picnic"
    Then The response code is 401

  Scenario: A student cannot delete another student's event
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    When I delete the event titled "Welcome picnic"
    Then The response code is 404

  Scenario: An admin can update another student's event
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as an admin
    When I update the event titled "Welcome picnic" with:
      | title | Moderated picnic |
    Then The response code is 200

  Scenario: An admin can delete another student's event
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as an admin
    When I delete the event titled "Welcome picnic"
    Then The response code is 204