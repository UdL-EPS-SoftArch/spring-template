Feature: Search public events
  In order to quickly find activities I am interested in
  As a visitor
  I want to search public events by keyword

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"
    And a category named "Culture" exists
    And a subcategory named "Museums" belongs to category "Culture"

  Scenario: An unauthenticated visitor searches public events by keyword
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    And an event titled "Museum visit" exists in subcategory "Museums"
    When I search public events for "picnic"
    Then The response code is 200
    And the public events include "Welcome picnic"
    And the public events do not include "Museum visit"

  Scenario: Keyword search ignores letter case
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    When I search public events for "PICNIC"
    Then The response code is 200
    And the public events include "Welcome picnic"

  Scenario: A keyword search with no matches returns an empty list
    Given I'm not logged in
    And an event titled "Welcome picnic" exists in subcategory "Meetups"
    When I search public events for "concert"
    Then The response code is 200
    And the public events list is empty

  Scenario: A keyword search within a category with no matches returns an empty list
    Given I'm not logged in
    And an event titled "Museum visit" exists in subcategory "Museums"
    When I search public events for "picnic" in category "Culture"
    Then The response code is 200
    And the public events list is empty
