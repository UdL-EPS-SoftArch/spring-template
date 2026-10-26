Feature: View public events
  In order to find activities to attend
  As a visitor
  I want to view the public events

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: An unauthenticated visitor views public events
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    When I view the public events
    Then The response code is 200
    And the public events include "Welcome picnic"

  Scenario: A visitor cannot view an event that does not exist
    Given I'm not logged in
    When I view the event titled "Missing event"
    Then The response code is 404

  Scenario: A visitor sees an empty list when there are no events
    Given I'm not logged in
    When I view the public events
    Then The response code is 200
    And the public events list is empty
