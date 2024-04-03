Feature: Retrieve supplier
  In order to retrieve a supplier
  As a user
  I want to be able to retrieve a supplier

  Scenario: List all suppliers
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And There is a registered supplier with username "supplier2", email "supplier2@sample.app" and password "password"
    And I login as "demo" with password "password"
    When I list all the existing suppliers
    Then The response code is 200
    And There are 3 suppliers being retrieved


  Scenario: List empty suppliers list
    Given I login as "demo" with password "password"
    When I list all the existing suppliers
    Then The response code is 200
    And There are 1 suppliers being retrieved


  Scenario: List suppliers list when not authenticated
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    When I list all the existing suppliers
    Then The response code is 401


  Scenario: Get an existing supplier
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "demo" with password "password"
    When I retrieve the supplier with username "supplier"
    Then The response code is 200
    And It has been retrieve the supplier with username "supplier"


  Scenario: Get a non existing supplier
    Given I login as "demo" with password "password"
    When I retrieve the supplier with username "supplier"
    Then The response code is 404


  Scenario: Get an existing supplier when not authenticated
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    When I retrieve the supplier with username "supplier"
    Then The response code is 401