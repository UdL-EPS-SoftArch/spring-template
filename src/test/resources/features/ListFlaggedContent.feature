Feature: List flagged content
  In order to review content that users have reported
  As an admin
  I want to list the flagged content

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: An admin lists flagged content
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And the event titled "Welcome picnic" has been reported by "luca"
    And I login as an admin
    When I list the flagged content
    Then The response code is 200
    And the flagged content includes the event titled "Welcome picnic"

  Scenario: A student cannot list flagged content
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I list the flagged content
    Then The response code is 403

  Scenario: An unauthenticated visitor cannot list flagged content
    Given I'm not logged in
    When I list the flagged content
    Then The response code is 401
