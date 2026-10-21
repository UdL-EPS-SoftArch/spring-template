Feature: Delete event
  In order to remove events that are no longer taking place
  As an event owner or admin
  I want to delete events

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

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

  Scenario: An admin can delete another student's event
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as an admin
    When I delete the event titled "Welcome picnic"
    Then The response code is 204
