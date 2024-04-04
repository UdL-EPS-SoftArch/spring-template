Feature: Retrieve Column
  In order to retrieve a column
  As a supplier
  I want to retrieve a column from mapping

  Background:
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    And I create a new mapping with name "CEP-2021-S1-WEIGHT.csv"
    And It has been created a new mapping with name "CEP-2021-S1-WEIGHT.csv" and is owned by "supplier"
    When I create a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then It has been created a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200


  Scenario: Retrieve a column
    When I retrieve the column with title "Date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then The response code is 200
    And The column retrieved has ontology uri "http://schema.org/"
    And The column retrieved has ontology type "date"
    And The column retrieved has data type "xsd:date"

  Scenario: Retrieve a column that does not exist
    When I retrieve the column with title "nonExistentColumn" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then The response code is 404