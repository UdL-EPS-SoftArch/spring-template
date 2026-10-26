Feature: Post comment
  In order to discuss activities
  As an Erasmus student
  I want to post comments on events

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

  Scenario: A student cannot comment on an event that does not exist
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I post a comment with content "Hello" on the event titled "Missing event"
    Then The response code is 400
