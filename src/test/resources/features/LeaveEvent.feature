Feature: Leave event
  In order to stop attending activities I can no longer make
  As a registered Erasmus student
  I want to leave events I have joined

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

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

  Scenario: An unauthenticated visitor cannot leave an event
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I leave the event titled "Welcome picnic"
    Then The response code is 401

  Scenario: A student cannot leave an event that does not exist
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I leave an event that does not exist
    Then The response code is 404
