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
