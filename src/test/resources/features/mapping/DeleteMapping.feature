Feature: Delete Mapping
  In order to delete a mapping
  As a supplier
  I want to delete a mapping

  Background:
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    And I create a new mapping with name "mapping"

  Scenario: Supplier deletes his mapping
    When I delete the mapping with id "1"
    Then The response code is 200


  Scenario: Supplier deletes a not owned mapping
    Given There is a registered supplier with username "supplier2", email "supplier2@sample.app" and password "password"
    And I login as "supplier2" with password "password"
    When I delete the mapping with id "1"
    Then The response code is 403
    And I login as "supplier" with password "password"
    And I retrieve the mapping with id "1"
    And The response code is 200


  Scenario: Delete a mapping when not authenticated
    Given I'm not logged in
    When I delete the mapping with id "1"
    Then The response code is 401
    And I login as "supplier" with password "password"
    And I retrieve the mapping with id "1"
    And The response code is 200


  Scenario: Delete a mapping that does not exist
    And I login as "supplier" with password "password"
    When I delete the mapping with id "999"
    Then The response code is 404


  Scenario: User deletes a mapping
    Given I login as "demo" with password "password"
    When I delete the mapping with id "1"
    Then The response code is 403
    And I login as "supplier" with password "password"
    And I retrieve the mapping with id "1"
    And The response code is 200