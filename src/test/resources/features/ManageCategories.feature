Feature: Manage categories
  In order to keep event categories consistent
  As an administrator
  I want only admins to create and delete categories

  Scenario: A standard user cannot create a category
    Given There is a registered user with username "category-user" and password "password" and email "category-user@sample.app"
    And I login as "category-user" with password "password"
    When I create a category named "User-created category"
    Then The response code is 403
    And the category named "User-created category" has not been created

  Scenario: An unauthenticated visitor cannot create a category
    Given I'm not logged in
    When I create a category named "Visitor-created category"
    Then The response code is 401
    And the category named "Visitor-created category" has not been created

  Scenario: An admin can create a category
    Given an admin user exists
    And I login as an admin
    When I create a category named "Admin-created category"
    Then The response code is 201
    And the category named "Admin-created category" has been created

  Scenario: A standard user cannot delete a category
    Given a category named "Protected category" exists
    And There is a registered user with username "category-user" and password "password" and email "category-user@sample.app"
    And I login as "category-user" with password "password"
    When I delete the category named "Protected category"
    Then The response code is 403
    And the category named "Protected category" still exists

  Scenario: An admin can delete a category
    Given a category named "Admin-deleted category" exists
    And an admin user exists
    And I login as an admin
    When I delete the category named "Admin-deleted category"
    Then The response code is 200
    And the category named "Admin-deleted category" no longer exists
