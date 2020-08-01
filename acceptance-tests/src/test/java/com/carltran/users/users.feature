Feature: Get users within 60 miles from London

  Background:
    * url 'http://localhost:8080/'

  Scenario: Get all users and then get the first user by id
    Given path 'users'
    When method get
    Then status 200
    And match $.data == '#notnull'
    And match $.error == '#null'
    And print response
  