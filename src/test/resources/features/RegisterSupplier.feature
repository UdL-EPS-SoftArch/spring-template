Feature: Register a Supplier
  In order to register a supplier
  As a supplier
  I want to be able to register

  Scenario: Register a supplier
    Given There is no registered supplier with username "supplier"
    And I'm not logged in
    When I register a new supplier with username "supplier", email "supplier@test.com" and password "password"
    Then The response code is 201
    And It has been created a user with username "supplier" and email "supplier@test.com", the password is not returned
    And I can login with username "supplier" and password "password"