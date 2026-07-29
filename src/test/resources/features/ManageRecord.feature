Feature: Manage Record
    In order to manage user records
    As a user
    I want to be able to create, retrieve, edit and delete my records

    Scenario: Create an owned record
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        When I create a new record with name "My Record" owned by "user"
        Then The response code is 201
        And The new record is owned by "user"
        And The list of records owned by "user" includes one named "My Record"

    Scenario: Retrieve an owned record
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And I create a new record with name "My Record" owned by "user"
        When I retrieve the record with name "My Record"
        Then The response code is 200

    # TODO: other scenarios, like not possible to access a record by another user...
