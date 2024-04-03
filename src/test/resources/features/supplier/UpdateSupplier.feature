Feature: Update Supplier
  In order to keep the profile information up to date
  As a supplier
  I want to update the profile information

  Scenario: Update supplier email
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I update the email of supplier "supplier" to "newEmail@sample.app"
    Then The response code is 200
    And It has been updated the email of supplier "supplier" to "newEmail@sample.app"


  Scenario: Supplier updates email to an invalid one
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I update the email of supplier "supplier" to "newEmail@"
    Then The response code is 400
    And The error message is "must be a well-formed email address"

  Scenario: Supplier updates email to a blank one
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I update the email of supplier "supplier" to ""
    Then The response code is 400
    And The error message is "must not be blank"


  Scenario: Update the email of a supplier that does not exist
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I update the email of supplier "nonexistentSupplier" to "newEmail@sample.app"
    Then The response code is 404


  Scenario: User updates a supplier email
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And I login as "demo" with password "password"
    When I update the email of supplier "supplier" to "newEmail@sample.app"
    Then The response code is 403


  Scenario: Supplier updates email of a not owned account
    Given There is a registered supplier with username "supplier", email "supplier@sample.app" and password "password"
    And There is a registered supplier with username "supplier2", email "supplier2@sample.app" and password "password"
    And I login as "supplier" with password "password"
    When I update the email of supplier "supplier2" to "newEmail@sample.app"
    Then The response code is 403
