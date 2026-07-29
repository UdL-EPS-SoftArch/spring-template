Feature: Manage Record
    In order to manage user records
    As a user
    I want to be able to create, retrieve, edit and delete my records

    Scenario: Create an owned record
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        When I create a new record with name "My Record"
        Then The response code is 201
        And The new record is owned by "user"
        And The list of records owned by "user" includes one named "My Record"

    Scenario: Retrieve an owned record
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And I create a new record with name "My Record"
        When I retrieve the record with name "My Record"
        Then The response code is 200
        And The retrieved record has name "My Record"

    Scenario: Cannot retrieve a record owned by another user
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And I create a new record with name "My Record"
        And There is a registered user with username "another" and password "password" and email "another@sample.app"
        And I login as "another" with password "password"
        When I retrieve the record with name "My Record"
        Then The response code is 404

    # TODO: Can retrieve if the other user makes it public