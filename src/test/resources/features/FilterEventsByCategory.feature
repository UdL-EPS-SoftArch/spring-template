Feature: Filter events by category
  In order to browse activities of a given interest
  As a visitor
  I want to filter public events by category

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"
    And a category named "Culture" exists
    And a subcategory named "Museums" belongs to category "Culture"

  Scenario: A visitor filters public events by category
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    And an event titled "Museum visit" exists in subcategory "Museums"
    When I filter public events by category "Social"
    Then The response code is 200
    And the public events include "Welcome picnic"
    And the public events do not include "Museum visit"

  Scenario: Category filtering ignores letter case
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    When I filter public events by category "social"
    Then The response code is 200
    And the public events include "Welcome picnic"

  Scenario: Filtering by a category with no events returns an empty list
    Given I'm not logged in
    And no events exist in category "Culture"
    When I filter public events by category "Culture"
    Then The response code is 200
    And the public events list is empty
