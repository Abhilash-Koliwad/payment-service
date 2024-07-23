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
- **Branches** - A,B,C,D,E,F<br>
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
The logic to find the cheapest way to make a payment between two branches is implemented using Dijkstra's algorithm in `PaymentService` class

### REST API Endpoint
The API for processing payments is exposed in the `PaymentResource` class which is designed to find the cheapest payment route between two branches.

- **Method**: POST
- **Path**: `/payment`
- **Request Body**:
  ```json
  {
    "originBranchId": "UUID",
    "destinationBranchId": "UUID"
  }

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
