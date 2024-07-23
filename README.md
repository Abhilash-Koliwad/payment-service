# Payment Service Documentation

### Overview
The project is implemented in Java & Spring Boot leveraging 
- H2 database - In memory database. 
- Liquibase - Tool to create database tables and load data.
- Hibernate - ORM Framework to map Java objects to database tables.
- REST APIs - To interact with application using HTTP requests.
- JUnit & Mockito - Implementing tests.

### Database 
Application is configured with H2 in-memory SQL database with the below tables,
- **Branch**: Representing a bank branch
  - Columns - id, name, transfer_cost, created_at, updated_at, version
- **Branch_Connection**: Representing connection between branches 
  - Columns - id, origin_branch_id, destination_branch_id, created_at, updated_at, version<br>

The data mentioned in the problem statement is already loaded into the tables via liquibase changelog i.e.,<br>
- **Branches** 
  - A (ID: 60cd3195-b2c6-4d47-8ae6-a3cf4c99c3c0)
  - B (ID: b9495c1b-a66f-4d57-b59b-4f7204751be7)
  - C (ID: 10bed16b-609d-45ce-afec-66963cc7c552)
  - D (ID: f3fba5c7-4e13-457a-b686-ead00aba1e66)
  - E (ID: 98e63497-dabb-4d69-80fb-9411801f49e3)
  - F (ID: 55b69417-852a-46a4-838c-7d97c5c6d54b) 
- **Branch Connections**, 
  - A to B 
  - A to C
  - C to B
  - B to D
  - C to E
  - D to E
  - E to D
  - D to F
  - E to F

### Core logic
The logic to find the cheapest way to make a payment between two branches is implemented using Dijkstra's algorithm in `PaymentService` class.

### Application Tests
- **PaymentResourceIntegrationTest** - Spring boot integration test class covering all the cases. (Direct branch connection, Indirect branch connection, No branch connection)<br>
- **PaymentResourceTest** - Unit test class for `PaymentResource`.<br>
- **PaymentServiceTest** - Unit test class for `PaymentService` covering all the cases. (Direct branch connection, Indirect branch connection, No branch connection)

### Running the Application
- **Clone the Repository**
  ```sh 
  git clone https://github.com/Abhilash-Koliwad/payment-service.git
- Navigate to project directory
  ```sh
  cd payment-service
- Build the project
  ```sh
  mvn clean install
- Run the application
  ```sh
  mvn spring-boot:run

### REST API Endpoint
The API for processing payments is exposed in the `PaymentResource` class which is designed to find the cheapest payment route between two branches.

- **Method**: POST
- **Path**: `/payment/process`
- **Request Body**:
  ```json
  {
    "originBranchId": "UUID",
    "destinationBranchId": "UUID"
  }
- **Response Body**:
  ```json
  {
    "branchSequence": "String"
  }

### Sample API Requests
Below are the API requests and responses while running the application in local,
- **Direct branch connection - A->B**
    <br><i>Request</i><br>
    ```
    POST http://localhost:8080/payment/process
    Accept: application/json
    Content-Type: application/json
    {
      "originBranchId": "60cd3195-b2c6-4d47-8ae6-a3cf4c99c3c0",
      "destinationBranchId": "b9495c1b-a66f-4d57-b59b-4f7204751be7"
    }
    ```
  <i>Response</i>
    ```
    {
      "branchSequence": "A,B"
    }
    ```

- **Indirect branch connection - A->D**
  <br><i>Request</i><br>
    ```
    POST http://localhost:8080/payment/process
    Accept: application/json
    Content-Type: application/json
    {
      "originBranchId": "60cd3195-b2c6-4d47-8ae6-a3cf4c99c3c0",
      "destinationBranchId": "f3fba5c7-4e13-457a-b686-ead00aba1e66"
    }
    ```
  <i>Response</i>
    ```
    {
      "branchSequence": "A,C,E,D"
    }
    ```

- **No branch connection - E->A**
  <br><i>Request</i><br>
    ```
    POST http://localhost:8080/payment/process
    Accept: application/json
    Content-Type: application/json
    {
      "originBranchId": "98e63497-dabb-4d69-80fb-9411801f49e3",
      "destinationBranchId": "60cd3195-b2c6-4d47-8ae6-a3cf4c99c3c0"
    }
    ```
  <i>Response</i>
    ```
    {
      "branchSequence": null
    }
    ```
