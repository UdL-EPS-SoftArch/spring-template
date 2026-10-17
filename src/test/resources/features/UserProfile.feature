Feature: User profiles
  In order to view and maintain account information
  As a registered Erasmus student
  I want to view profiles and edit my own profile

  Scenario: A student views their own profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I view the profile of "amina"
    Then The response code is 200
    And the profile has username "amina"
    And the profile has email "amina@sample.app"
    And the profile does not expose the password

  Scenario: A student views another student's profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "amina" with password "password"
    When I view the profile of "luca"
    Then The response code is 200
    And the profile has username "luca"
    And the profile has email "luca@sample.app"
    And the profile does not expose the password

  Scenario: An unauthenticated visitor cannot view a profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I'm not logged in
    When I view the profile of "amina"
    Then The response code is 401

  Scenario: A student updates their own profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And I login as "amina" with password "password"
    When I update my profile with:
      | username | amina-h       |
      | fullName | Amina Hassan  |
      | email    | amina.h@sample.app |
      | password | new-password |
    Then The response code is 200
    And the profile has username "amina-h"
    And the profile has full name "Amina Hassan"
    And the profile has email "amina.h@sample.app"
    And the profile does not expose the password

  Scenario: A student cannot update another student's profile
    Given There is a registered user with username "amina" and password "password" and email "amina@sample.app"
    And There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "amina" with password "password"
    When I update the profile of "luca" with:
      | fullName | Luca Rossi |
    Then The response code is 404