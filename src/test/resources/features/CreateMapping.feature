Feature: Create a Mapping of the Data
  In order to create a mapping of the data
  As a provider
  I want to create a mapping of the data

  Scenario: Create a mapping of the data
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I create a new mapping with name "Sample Mapping"
    Then The response code is 201
    And It has been created a new mapping with name "Sample Mapping" and is owned by "supplier"

  Scenario: Create a mapping of the data with empty name
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I create a new mapping with name ""
    Then The response code is 400
    And The error message is "Invalid request content."
    And It has not been created a new mapping