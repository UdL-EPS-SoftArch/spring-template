Feature: Upload Mapping file
  In order to upload a mapping file
  As a supplier
  I want to be able to upload a mapping file

  Background:
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    And I create a new mapping with name "CEP-2021-S1-WEIGHT.csv"
    And It has been created a new mapping with name "CEP-2021-S1-WEIGHT.csv" and is owned by "supplier"


  Scenario: Supplier upload a mapping file
    Given I login as "supplier" with password "password"
    When I upload a mapping file with title "CEP-2021-S1-WEIGHT.csv"
    Then The mapping repository should have a file with title "CEP-2021-S1-WEIGHT.csv"
    And The mapping content should be "CEP-2021-S1-WEIGHT.csv" file content