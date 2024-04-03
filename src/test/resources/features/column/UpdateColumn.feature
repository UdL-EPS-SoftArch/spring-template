Feature: Update Column
  In to update the column
  As a supplier
  I want to update the column

  Background:
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    And I create a new mapping with name "mapping1"
    And It has been created a new mapping with name "mapping1" and is owned by "supplier"
    And I create a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "mapping1"

  Scenario: Update the date column for the new title
    When I update the column with title "Date" to new title "New Date" for the mapping with id 1
    Then The response code is 200


  Scenario: Update the date column for the new ontology uri
    When I update the column with title "Date" to new ontology uri "http://newUri.org/" for the mapping with id 1
    Then The response code is 200

  Scenario: Update the date column for the new ontology
    When I update the column with title "Date" to new ontology type "dateTime" for the mapping with id 1
    Then The response code is 200


  Scenario: Update the date column for the new data type
    When I update the column with title "Date" to new data type "xsd:dateTime" for the mapping with id 1
    Then The response code is 200