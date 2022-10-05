Feature: Create a Request
  In order to use the app
  As a user
  I want to create a new Request


  #Scenario: Register new request
    # Given I'm not logged in
    # When I register a new request with name "croquetas", price "50", description "tremenda croqueta bro, por la gloria de mi mama" and requester "user"
    # Then Server responds with page containing "You are not logged in"
    # And There is "Log in" link available

  Background:
    Given There is a registered user with username "mama" and password "123" and email "cocina@lamama.com"
    And Exists an offer with name "croquetas", price "50", description "croquetas caseras mu ricas de la mama" and offererUser "mama"

  Scenario:
    Given I login as "mama" with password "123"
    And I find an offer with name "croquetas", price "50", description "croquetas caseras mu ricas de la mama" and offererUser "mama"
    Then I create a request
    And The response code is 201