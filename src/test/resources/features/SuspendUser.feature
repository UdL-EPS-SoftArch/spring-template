Feature: Suspend user
  In order to protect the community from abusive users
  As an admin
  I want to suspend user accounts

  Scenario: An admin suspends a user
    Given an admin user exists
    And There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as an admin
    When I suspend the user "amina"
    Then The response code is 200
    And the user "amina" is suspended

  Scenario: A student cannot suspend another user
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I suspend the user "amina"
    Then The response code is 403
    And the user "amina" is not suspended

  Scenario: An unauthenticated visitor cannot suspend a user
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I'm not logged in
    When I suspend the user "amina"
    Then The response code is 401
    And the user "amina" is not suspended

  Scenario: An admin cannot suspend a user that does not exist
    Given an admin user exists
    And I login as an admin
    When I suspend the user "ghost"
    Then The response code is 404

  Scenario: Suspending an already suspended user is idempotent
    Given an admin user exists
    And There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And the admin has suspended user "amina"
    And I login as an admin
    When I suspend the user "amina"
    Then The response code is 200
    And the user "amina" is suspended

  Scenario: An admin reactivates a suspended user
    Given an admin user exists
    And There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And the admin has suspended user "amina"
    And I login as an admin
    When I reactivate the user "amina"
    Then The response code is 200
    And the user "amina" is not suspended
    And I can login with username "amina" and password "password"

  Scenario: A student cannot reactivate a suspended user
    Given an admin user exists
    And There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And the admin has suspended user "amina"
    And I login as "luca" with password "password"
    When I reactivate the user "amina"
    Then The response code is 403
    And the user "amina" is suspended

  Scenario: An admin cannot reactivate a user that does not exist
    Given an admin user exists
    And I login as an admin
    When I reactivate the user "ghost"
    Then The response code is 404
