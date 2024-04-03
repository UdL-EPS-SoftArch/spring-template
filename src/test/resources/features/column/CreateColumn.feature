Feature: Create a Column of the mapping
  In order to create a column of the mapping
  As a supplier
  I want to create a column of the mapping

  Background:
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    And I create a new mapping with name "CEP-2021-S1-WEIGHT.csv"
    And It has been created a new mapping with name "CEP-2021-S1-WEIGHT.csv" and is owned by "supplier"

    Scenario: Create a column of the mapping
      When I create a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
      Then It has been created a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
      And The response code is 200
