Feature: Logout
  In order to protect my account on shared devices
  As an Erasmus student or admin
  I want to log out securely

  Scenario: An authenticated user logs out
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I logout
    Then The response code is 302
    And I am redirected to the login or home page
    And I am not authenticated
    And I cannot access protected resources

  Scenario: An admin logs out
    Given an admin user exists
    And I login as an admin
    When I logout
    Then The response code is 302
    And I am not authenticated

  Scenario: Logging out while unauthenticated is idempotent
    Given I'm not logged in
    When I logout
    Then The response code is 302
    And I am redirected to the login or home page
    And I am not authenticated
