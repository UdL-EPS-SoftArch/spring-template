Feature: Report inappropriate content
  In order to help keep the community safe
  As a registered Erasmus student
  I want to report inappropriate events and comments

  Background:
    Given a category named "Social" exists
    And a subcategory named "Meetups" belongs to category "Social"

  Scenario: An authenticated user successfully reports an event
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And I login as "luca" with password "password"
    When I report the event titled "Welcome picnic"
    Then I see a confirmation that my report was submitted

  Scenario: An authenticated user successfully reports a comment
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
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


  Scenario: An authenticated user cannot report the same comment twice
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And a comment with content "Inappropriate comment" exists on the event titled "Welcome picnic" and is authored by "amina"
    And I login as "luca" with password "password"
    And I have already reported the comment with content "Inappropriate comment"
    When I report the comment with content "Inappropriate comment"
    Then I see a warning that I have already reported this content
    And only one report from "luca" exists for the comment with content "Inappropriate comment"

  Scenario: A report must target an existing event
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I report an event that does not exist
    Then The response code is 400

  Scenario: A report without a target is rejected
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And I login as "luca" with password "password"
    When I submit a report without a target
    Then The response code is 400

  Scenario: A report cannot target an event and a comment at the same time
    Given There is a registered user with username "luca" and password "password" and email "luca@sample.app"
    And an event titled "Welcome picnic" exists and is owned by "amina"
    And a comment with content "Inappropriate comment" exists on the event titled "Welcome picnic" and is authored by "amina"
    And I login as "luca" with password "password"
    When I report both the event titled "Welcome picnic" and the comment with content "Inappropriate comment"
    Then The response code is 400
