Feature: Edit profile
  In order to maintain my account information
  As a registered Erasmus student
  I want to edit my own profile

  Scenario: A student updates their own profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I update my profile with:
      | fullName | Amina Hassan  |
      | email    | amina.h@sample.app |
      | password | new-password |
    Then The response code is 200
    And the profile has username "amina"
    And the profile has full name "Amina Hassan"
    And the profile has email "amina.h@sample.app"
    And the profile does not expose the password

  Scenario: A student changes their password and logs in with the new one
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I update my profile with:
      | password | new-password |
    Then The response code is 200
    And I can login with username "amina" and password "new-password"
    And I cannot login with username "amina" and password "password"

  Scenario: A student cannot set a password shorter than 8 characters
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I update my profile with:
      | password | short |
    Then The response code is 400

  Scenario: A student cannot change their username
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I update my profile with:
      | username | amina-h |
    Then The response code is 400
    And The error message is "Username cannot be changed"

  Scenario: A student cannot update another student's profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "amina" with password "password"
    When I update the profile of "luca" with:
      | fullName | Luca Rossi |
    Then The response code is 404