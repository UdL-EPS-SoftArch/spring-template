Feature: Retrieve Mapping
  In order to retrieve the mapping
  As a supplier
  I want to be able to retrieve all the mapping or a specific mapping

  Scenario: User retrieves and list all the mappings
    Given There is a created mapping with the title "Mapping 1" and provided by "demoSupplier"
    And There is a created mapping with the title "Mapping 2" and provided by "demoSupplier"
    And I login as "demo" with password "password"
    When I list all the existing mapping
    Then The response code is 200
    And There are 2 mappings being retrieved

  Scenario: Provider retrieves and list all the mappings
    Given There is a created mapping with the title "Mapping 1" and provided by "demoSupplier"
    And There is a created mapping with the title "Mapping 2" and provided by "demoSupplier"
    And I login as "demoSupplier" with password "password"
    When I list all the existing mapping
    Then The response code is 200
    And There are 2 mappings being retrieved

  Scenario: User list empty mappings list
    Given I login as "demo" with password "password"
    When I list all the existing mapping
    Then The response code is 200
    And There are 0 mappings being retrieved

  Scenario: List all the mappings when not logged in
    Given There is a created mapping with the title "Mapping 1" and provided by "demoSupplier"
    And There is a created mapping with the title "Mapping 2" and provided by "demoSupplier"
    When I list all the existing mapping
    Then The response code is 401
    And The error message is "Unauthorized"

  Scenario: User get a non existing mapping
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I retrieve the mapping with id "999"
    Then The response code is 404

  Scenario: Get a mapping when not logged in
    Given There is a created mapping with the title "Mapping 1" and provided by "demoSupplier"
    And I'm not logged in
    When I retrieve the mapping with id "1"
    Then The response code is 401
    And The error message is "Authorization required"

  Scenario: List all the mappings containing in title
    Given There is a created mapping with the title "Mapping1" and provided by "demoSupplier"
    And There is a created mapping with the title "Mapping2" and provided by "demoSupplier"
    And There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I list all the existing mapping with title containing "Mapping"
    Then The response code is 200
    And There are 2 mappings being retrieved


  Scenario: Retrieve a mapping by title that does not exist
    Given There is a created mapping with the title "Mapping" and provided by "demoSupplier"
    And There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I retrieve the mapping with title "Mapping 1"
    Then The response code is 200
    And There are 0 mappings being retrieved

  Scenario: Supplier get a mapping by id
    Given There is a created mapping with the title "Mapping 1" and provided by "demoSupplier"
    And I login as "demoSupplier" with password "password"
    When I retrieve the mapping with id "1"
    Then The response code is 200

  Scenario: Supplier get a mapping that not belong to him
    Given There is a registered supplier with username "supplier1", email "supplier1@sample.app" and password "password"
    And There is a registered supplier with username "supplier2", email "supplier2@sample.app" and password "password"
    And I login as "supplier1" with password "password"
    And I create a new mapping with name "Mapping1"
    And I login as "supplier2" with password "password"
    When I retrieve the mapping with id "1"
    Then The response code is 401
    And The error message is "Authorization required"