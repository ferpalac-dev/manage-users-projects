# Rest API for managing users and projects

SpringBoot app using [Spring Data REST](https://spring.io/projects/spring-data-rest) for managing crud and search 
operations on users and projects entities.

## How to run the SpringBoot app with Maven
`mvn spring-boot:run`

## How to run the test with Maven
`mvn clean test`

Integration test cases are using [RestAssured](https://rest-assured.io/) for validating REST services.

### Curl operations samples:
* create user
```
curl --location 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "email": "test1@test.com"
}'
```
* list users
```
curl --location 'http://localhost:8080/users'
```
* get user by id
```curl --location 'http://localhost:8080/users/1'```
* delete user by id
```
curl --location --request DELETE 'http://localhost:8080/users/1'
```