Feature: Remove inappropriate event
  In order to keep the platform safe and respectful
  As an admin
  I want to remove events that have been flagged as inappropriate

  Scenario: An admin removes an inappropriate event that was flagged
    Given an admin user exists
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And the event titled "Welcome picnic" has been reported by "luca"
    And I login as an admin
    When I delete the event titled "Welcome picnic"
    Then The response code is 204
    And the event titled "Welcome picnic" does not exist
    And the flagged content does not include the event titled "Welcome picnic"
