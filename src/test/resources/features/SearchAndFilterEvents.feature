Feature: Search and filter events
  In order to quickly find activities I am interested in
  As a registered Erasmus student
  I want to search events by keyword and combine the search with a category filter

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"
    And a category named "Culture" exists
    And a subcategory named "Museums" belongs to category "Culture"

  Scenario: An Erasmus student combines a keyword search with a category filter
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    And an event titled "Picnic at the museum" exists in subcategory "Museums"
    When I search public events for "picnic" in category "Social"
    Then The response code is 200
    And the public events include "Welcome picnic"
    And the public events do not include "Picnic at the museum"
