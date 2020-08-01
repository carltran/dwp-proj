# DWP test project

## Introduction
This project is a maven project. It provides a REST service with single endpoint `http://localhost:8080/users` 
to allow retrieval of users with coordinates within 60 miles from London. Results is a list of users in JSON.

It makes use of an existing APIs to query lists of users, filter and query full details of users matching criteria.

## Assumptions
London's coordinates are hard-coded in the controller.
Existing API is configurable in `application.properties`.

## How to test
use `mvn surefire:test` to run all unit tests and integration tests.

They should all pass.

## How to run
Only run this after running tests.
use `mvn spring-boot:run` to start the service

## End-to-end test
This assumes that service under test is already running. Hence use the step above to run it first. 
Then go to directory `acceptance-tests` and use `mvn test` to run e2e karate tests.