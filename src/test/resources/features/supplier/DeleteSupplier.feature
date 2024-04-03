Feature: Delete supplier
  In order to delete a supplier
  As a supplier
  I want to delete my supplier account

  Scenario: Supplier deletes his account
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I delete the supplier with username "supplier"
    Then The response code is 200
    And It does not exist a supplier with username "supplier"


  Scenario: Supplier deletes a not owned account
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And There is a registered supplier with username "supplier2", email "supplier2@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I delete the supplier with username "supplier2"
    Then The response code is 403
    And It still exists a supplier with username "supplier2"


  Scenario: Delete a supplier when not authenticated
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    When I delete the supplier with username "supplier"
    Then The response code is 401
    And It still exists a supplier with username "supplier"


  Scenario: Delete a supplier that does not exist
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I delete the supplier with username "nonexistentSupplier"
    Then The response code is 404

  Scenario: User deletes a supplier
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "demo" with password "password"
    When I delete the supplier with username "supplier"
    Then The response code is 403
    And It still exists a supplier with username "supplier"