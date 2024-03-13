Feature: Register a Supplier
  In order to register a supplier
  As a supplier
  I want to be able to register

  Scenario: Register a supplier
    Given There is no registered supplier with username "supplier"
    And I'm not logged in
    When I register a new supplier with username "supplier", email "supplier@sample.app" and password "password"
    Then The response code is 201
    And It has been created a user with username "supplier" and email "supplier@sample.app", the password is not returned
    And I can login with username "supplier" and password "password"


  Scenario: Register a supplier with an existing username
    Given There is a registered supplier with username "supplier", email "existingSupplier@sample.app" and password "existingSupplierPassword"
    And I'm not logged in
    When I register a new supplier with username "supplier", email "supplier@test.com" and password "password"
    Then The response code is 409
    And I cannot login with username "supplier" and password "password"


  Scenario: Register a supplier already logged in
    Given I login as "demo" with password "password"
    When I register a new supplier with username "supplier", email "supplier@sample.app" and password "password"
    Then The response code is 403
    And It has not found a supplier with username "supplier"


  Scenario: Register a supplier with an empty password
    Given I'm not logged in
    When I register a new supplier with username "supplier", email "supplier@sample.app" and password ""
    Then The response code is 400
    And The error message is "must not be blank"
    And It has not found a supplier with username "supplier"