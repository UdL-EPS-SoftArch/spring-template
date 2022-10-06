Feature: Create a Request
  In order to use the app
  As a user
  I want to create a new Request


  #Scenario: Register new request
    # Given I'm not logged in
    # When I register a new request with name "croquetas", price "50", description "tremenda croqueta bro, por la gloria de mi mama" and requester "user"
    # Then Server responds with page containing "You are not logged in"
    # And There is "Log in" link available


  Scenario: Create new request.
    Given I login as "user" with password "password"
    And There is an offer created
    When I Create a new request
    Then There is a request created
