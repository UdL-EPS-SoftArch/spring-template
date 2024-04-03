Feature: Testing then Yaml generator

  Scenario: Testing the Yaml generator
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"

    And I create a new mapping with name "CEP-2021-S1-WEIGHT.csv"
    And It has been created a new mapping with name "CEP-2021-S1-WEIGHT.csv" and is owned by "supplier"
    When I upload a mapping file with title "CEP-2021-S1-WEIGHT.csv" with prefixes "http://www.example.com/,http://myontology.com/,http://schema.org/" and main ontology "schema:Pork"
    Then The mapping repository should have a file with title "CEP-2021-S1-WEIGHT.csv"
    And The mapping content should be "CEP-2021-S1-WEIGHT.csv" file content

    When I create a new column with title "Animal ID", ontology uri "http://www.example.com/", ontology "id" and data type "xsd:integer" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then It has been created a new column with title "Animal ID", ontology uri "http://www.example.com/", ontology "id" and data type "xsd:integer" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200

    When I create a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then It has been created a new column with title "Date", ontology uri "http://schema.org/", ontology "date" and data type "xsd:date" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200

    When I create a new column with title "Weight", ontology uri "http://www.example.com/", ontology "weight" and data type "xsd:integer" for the mapping "CEP-2021-S1-WEIGHT.csv"
    Then It has been created a new column with title "Weight", ontology uri "http://www.example.com/", ontology "weight" and data type "xsd:integer" for the mapping "CEP-2021-S1-WEIGHT.csv"
    And The response code is 200

    Then Write the yaml file with mapping name "CEP-2021-S1-WEIGHT.csv"