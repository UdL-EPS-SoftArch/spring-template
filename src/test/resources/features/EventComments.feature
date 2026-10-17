Feature: Event comments
  In order to discuss activities
  As an Erasmus student
  I want to post and view comments on events

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: A logged-in student posts a comment on an event
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    When I post a comment with content "Looking forward to meeting everyone!" on the event titled "Welcome picnic"
    Then The response code is 201
    And the created comment has content "Looking forward to meeting everyone!"
    And the created comment is authored by "luca"
    And the created comment belongs to the event titled "Welcome picnic"

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

  Scenario: An unauthenticated visitor cannot post a comment
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I post a comment with content "Looking forward to meeting everyone!" on the event titled "Welcome picnic"
    Then The response code is 401

  Scenario: A student cannot post an empty comment
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    When I post a comment with content "" on the event titled "Welcome picnic"
    Then The response code is 400

  Scenario: A visitor cannot view comments for an event that does not exist
    Given I'm not logged in
    When I view comments for the event titled "Missing event"
    Then The response code is 404
    