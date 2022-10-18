<<<<<<< HEAD
Feature: Submit review
  In order to use the app
  As a buyer
  I want to submit a review to a seller

  Background:
    Given There is a registered user with username "user0" and password "password0" and email "user0@sample.app"
    Given There is a registered user with username "user1" and password "password1" and email "user1@sample.app"

  Scenario: Submit a new review
    Given I can login with username "user0" and password "password0"
    When The buyer submits a new review with username "user0", number of stars 5 and message "Great" to a seller "user1"
    Then The response code is 201
    And It has been submitted a review by buyer with username "user0"

  Scenario: Submit review when I am not authenticated
    Given I'm not logged in
    When The buyer submits a new review with username "user0", number of stars 5 and message "Great" to a seller "user1"
    Then The response code is 401
    And A new review has not been created

  Scenario: Submit review with empty field stars
    Given I can login with username "user0" and password "password0"
    When The buyer submits a new review with username "user0", number of stars 0 and message "Great" to a seller "user1"
    Then The response code is 400
    And The error message is "must be greater than or equal to 1"
    And A new review has not been created






Feature: Submit Review
  In order to use the App
  As a buyer
  I want to submit a review to a seller

  Background: I'm logged in
    Given There is no registered user with username "user0"
    And I'm not logged in
    When I register a new user with username "user0", email "user@sample.app" and password "password"
    Then The response code is 201
    And It has been created a user with username "user0" and email "user@sample.app", the password is not returned
    And I can login with username "user0" and password "password"

  Scenario: Submit a new review
    Given There is no review submitted by a buyer with username "user0" to a seller with username "user1"
    And Exists a transaction between the buyer with username "user0" and the seller with username "user1"
    When The buyer submits a new review with username "user0", number of stars 5 and message "Great"
    Then The response code is 201
    And It has been submitted a review for the seller with username "user1", number of stars 5 and message "Great"
    And It has been submitted a review by buyer with username "user0"

  Scenario: Submit a review without being logged in
    Given I'm not logged in
    When The buyer submits a new review with username "user0", number of stars 5 and message "Great!"
    Then The response code is 401
    And The review has not been created

  Scenario: Submit a review to user that already has one review submitted by the same user
    Given There is already a review submitted by a buyer with username "user0" to a seller with username "user1"
    When The buyer submits a new review with username "user0", number of stars 5 and message "Great!"
    Then The response code is 401
    And The review has not been created

  Scenario: Submit a review with empty field stars
    When The buyer submits a new review with username "user0" and message "Great!"
    Then The response code is 400
    And The error message is "You must to provide a number of stars in your review"
    And The review has not been created

  Scenario: Submit a review before having a transaction with user
    And I'm not logged in
    When The buyer submits a new review with username "user0", number of stars 5 and message "Great!"
    Then The response code is 400
    And The error message is "There was no transaction between users, you can't submit a review!"
    And The review has not been created
>>>>>>> reviews/scenarios
