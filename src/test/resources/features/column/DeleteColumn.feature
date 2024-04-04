Feature: Delete Column
  In order to delete a column
  As a supplier
  I want to delete a column


  Background:
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    And I create a new mapping with name "CEP-2021-S1-WEIGHT.csv"
    And It has been created a new mapping with name "CEP-2021-S1-WEIGHT.csv" and is owned by "supplier"
    When I create a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then It has been created a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200

  Scenario: Supplier deletes his column from mapping
    When I delete the column with title "Date" from the mapping "CEP-2021-S1-WEIGHT.csv"
    Then I retrieve the column with title "Date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then The response code is 404


  Scenario: Supplier deletes a not owned column from mapping
    Given There is a registered supplier with username "supplier2", email "supplier2@sample.app" and password "password"
    And I login as "supplier2" with password "password"
    When I delete the column with title "Date" from the mapping "CEP-2021-S1-WEIGHT.csv"
    Then The response code is 401
    And I login as "supplier" with password "password"
    And I retrieve the column with title "Date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200


  Scenario: Delete a column when not authenticated
    Given I'm not logged in
    When I delete the column with title "Date" from the mapping "CEP-2021-S1-WEIGHT.csv"
    Then The response code is 401
    And I login as "supplier" with password "password"
    And I retrieve the column with title "Date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200

  Scenario: User deletes a column
    Given I login as "demo" with password "password"
    When I delete the column with title "Date" from the mapping "CEP-2021-S1-WEIGHT.csv"
    Then The response code is 401
    And I login as "supplier" with password "password"
    And I retrieve the column with title "Date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200