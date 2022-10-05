Feature: Create an Offer
  In order to sell a product
  As a user
  I want to create a new offer

  Scenario: Create new offer
    Given There is no offer created from the user "user"
    And I'm logged in with user "user" and password "password"
    Then The offer should be created together with the announcement
    And which has a name "product1", description "product description" and a price "10".
    And a ZoneDateTime "dateTime" and a offerer user "user"
    And After all the steps I can retrieve this offer using the name "product1" and the offererUser "user"