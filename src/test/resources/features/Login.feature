Feature: Login
  In order to access the features available to my account
  As an Erasmus student or admin
  I want to log in securely

  Scenario: An Erasmus student logs in with valid credentials
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    When I login as "amina" with password "password"
    Then The response code is 200
    And I am authenticated as "amina"

  Scenario: An admin logs in with valid credentials
    Given an admin user exists with username "admin" and password "admin-password"
    When I login as "admin" with password "admin-password"
    Then The response code is 200
    And I am authenticated as "admin"

  Scenario: Login is rejected when the password is incorrect
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    When I login as "amina" with password "wrong-password"
    Then The response code is 401
    And I am not authenticated

  Scenario: Login is rejected for an unknown username
    Given I'm not logged in
    When I login as "unknown-user" with password "password"
    Then The response code is 401
    And I am not authenticated

  Scenario Outline: Login is rejected when a required credential is missing
    Given I'm not logged in
    When I login with "<username>" and password "<password>"
    Then The response code is 400
    And I am not authenticated

    Examples:
      | username | password |
      |          | password |
      | amina    |          |

  Scenario: A suspended user cannot log in
    Given an admin user exists
    And There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And the admin has suspended user "amina"
    When I login as "amina" with password "password"
    Then The response code is 403
    And I am not authenticated
