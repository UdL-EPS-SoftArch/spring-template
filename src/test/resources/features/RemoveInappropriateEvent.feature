Feature: Remove inappropriate event
  In order to keep the platform safe and respectful
  As an admin
  I want to remove events that have been flagged as inappropriate

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: An admin removes an inappropriate event that was flagged
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And the event titled "Welcome picnic" has been reported by "luca"
    And I login as an admin
    When I delete the event titled "Welcome picnic"
    Then The response code is 204
    And the event titled "Welcome picnic" does not exist
    And the flagged content does not include the event titled "Welcome picnic"

  Scenario: An admin removes a flagged comment and its reports disappear
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And a comment with content "Inappropriate comment" exists on the event titled "Welcome picnic" and is authored by "amina"
    And the comment with content "Inappropriate comment" has been reported by "luca"
    And I login as an admin
    When I delete the comment with content "Inappropriate comment"
    Then The response code is 204
    And the flagged content does not include the comment with content "Inappropriate comment"

  Scenario: A student cannot remove a flagged comment written by someone else
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And a comment with content "Inappropriate comment" exists on the event titled "Welcome picnic" and is authored by "amina"
    And I login as "luca" with password "password"
    When I delete the comment with content "Inappropriate comment"
    Then The response code is 403
    And the comment with content "Inappropriate comment" still exists
