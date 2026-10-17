Feature: Create event
  In order to organize activities for other Erasmus students
  As a registered student
  I want to create an event

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: A logged-in student creates an event
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I create an event with:
      | title           | Welcome picnic                    |
      | description     | Meet other Erasmus students       |
      | address         | City park                         |
      | dateTime        | 2030-11-10T16:00:00               |
      | maxParticipants | 20                                |
      | estimatedCost   | 5.00                              |
      | subcategory     | Meetups                           |
    Then The response code is 201
    And the created event has title "Welcome picnic"
    And the created event is owned by "amina"

  Scenario: An unauthenticated visitor cannot create an event
    Given I'm not logged in
    When I create an event with:
      | title           | Welcome picnic                    |
      | description     | Meet other Erasmus students       |
      | address         | City park                         |
      | dateTime        | 2030-11-10T16:00:00               |
      | maxParticipants | 20                                |
      | estimatedCost   | 5.00                              |
      | subcategory     | Meetups                           |
    Then The response code is 401

  Scenario Outline: An event cannot be created without a required field
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I create an event without "<field>"
    Then The response code is 400

    Examples:
      | field           |
      | title           |
      | description     |
      | address         |
      | dateTime        |
      | maxParticipants |
      | estimatedCost   |
      | subcategory     |
      