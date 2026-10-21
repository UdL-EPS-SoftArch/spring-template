Feature: View event locations
  In order to know where an activity takes place
  As a visitor
  I want to view the location of an event

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: An unauthenticated visitor views an event and its location
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups" at address "City park"
    When I view the event titled "Welcome picnic"
    Then The response code is 200
    And the event has address "City park"
