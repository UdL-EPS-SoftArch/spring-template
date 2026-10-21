Feature: Report inappropriate content
  In order to help keep the community safe
  As a registered Erasmus student
  I want to report inappropriate events and comments

  Scenario: An authenticated user successfully reports an event
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    When I report the event titled "Welcome picnic"
    Then I see a confirmation that my report was submitted

  Scenario: An authenticated user successfully reports a comment
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And a comment with content "Inappropriate comment" exists on the event titled "Welcome picnic" and is authored by "amina"
    And I login as "luca" with password "password"
    When I report the comment with content "Inappropriate comment"
    Then I see a confirmation that my report was submitted

  Scenario: An unauthenticated visitor is redirected to login or registration when reporting content
    Given I'm not logged in
    And an event titled "Welcome picnic" exists and is owned by "amina"
    When I report the event titled "Welcome picnic"
    Then I am redirected to the login or registration page
    And I see a message asking me to log in or register

  Scenario: An authenticated user cannot report the same content twice
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    And I have already reported the event titled "Welcome picnic"
    When I report the event titled "Welcome picnic"
    Then I see a warning that I have already reported this content
    And only one report from "luca" exists for the event titled "Welcome picnic"