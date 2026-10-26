Feature: View event comments
  In order to read the discussion about an activity
  As a visitor
  I want to view the comments posted on an event

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: A visitor views comments on a public event
    Given I'm not logged in
    And There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And a comment with content "Looking forward to meeting everyone!" exists on the event titled "Welcome picnic" and is authored by "luca"
    When I view comments for the event titled "Welcome picnic"
    Then The response code is 200
    And the comments include "Looking forward to meeting everyone!"
    And that comment is authored by "luca"

  Scenario: A visitor sees no comments for an event that does not exist
    Given I'm not logged in
    When I view comments for the event titled "Missing event"
    Then The response code is 200
    And the comments list is empty

  Scenario: A visitor views an event that has no comments
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I view comments for the event titled "Welcome picnic"
    Then The response code is 200
    And the comments list is empty
