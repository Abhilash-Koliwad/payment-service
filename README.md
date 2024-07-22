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
#### Branch (Columns - id, name, transfer_cost, created_at, updated_at, version)
#### Branch_Connection (Properties - id, origin_branch_id, destination_branch_id, created_at, updated_at, version)
The data mentioned in the problem statement is already loaded into the tables via liquibase changelog i.e.,<br>
Branches - A,B,C,D,E,F<br>
Branch Connections, 
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
The logic to find the cheapest way to make a payment between two branches is implemented using Dijkstra's algorithm in PaymentService.java class

### REST API
API to process payments is exposed in PaymentResource.

### Application Tests
PaymentResourceIntegrationTest - Spring boot integration test class covering all the cases. (Direct branch connection, Indirect branch connection, No branch connection)<br>
PaymentResourceTest - Unit test class for PaymentResource.<br>
PaymentServiceTest - Unit test class for PaymentResource covering all the cases. (Direct branch connection, Indirect branch connection, No branch connection)
