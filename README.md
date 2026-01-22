# Spring Data JPA Query By Example Demo

Transform your Spring Data JPA queries from complex boilerplate into elegant, type-safe search operations with [Query By Example](https://docs.spring.io/spring-data/jpa/reference/repositories/query-by-example.html). This project demonstrates how to implement dynamic, flexible queries without the overhead of writing multiple repository methods or complex JPQL statements.

## Overview

Query By Example (QBE) is a user-friendly querying technique that allows you to create dynamic queries using domain object instances as templates. This approach shines when building search functionality with multiple optional parameters, such as advanced search forms or dynamic filters.

## Project Requirements

- Java 23
- Spring Boot 3.3.5
- PostgreSQL
- Docker (for running the database)
- Maven

## Key Features

- Dynamic query generation using domain objects
- Type-safe query construction
- Minimal boilerplate code
- Integration with Spring Data JPA
- Docker-based development environment
- Comprehensive test coverage using TestContainers

## Getting Started

### Environment Setup

Ensure you have the following installed:
- Java 23 JDK
- Docker Desktop
- Maven

### Running the Application

1. Build and run the application:
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

## Understanding Query By Example

### Basic Example

Here's a simple example of how to use Query By Example:

```java
// Create a probe (example) entity
Employee probe = new Employee();
probe.setDepartment("IT");
probe.setPosition("Developer");

// Create the Example with the probe
Example<Employee> example = Example.of(probe);

// Find all matching employees
List<Employee> developers = employeeRepository.findAll(example);
```

### Advanced Usage

For more complex scenarios, you can customize the matching behavior:

```java
// Create a custom ExampleMatcher
ExampleMatcher matcher = ExampleMatcher.matching()
    .withIgnoreCase()
    .withStringMatcher(StringMatcher.CONTAINING);

Employee probe = new Employee();
probe.setDepartment("eng");  // Will match "Engineering"

Example<Employee> example = Example.of(probe, matcher);
List<Employee> engineers = employeeRepository.findAll(example);
```

## When to Use Query By Example

QBE is ideal for:

- ✅ Search forms with multiple optional filters
- ✅ Quick prototyping and development
- ✅ Simple equality-based queries
- ✅ Scenarios where search criteria are unknown at compile time

Consider alternatives when you need:

- ❌ Complex comparisons (>, <, BETWEEN)
- ❌ OR conditions
- ❌ Complex JOIN operations
- ❌ Custom SQL functions

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── dev/danvega/qbe/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       ├── application.yml
│       └── data.sql
└── test/
    └── java/
        └── dev/danvega/qbe/
```

## Configuration

The application's main configuration is in `application.yml`:

```yaml
spring:
  application:
    name: qbe
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop
```

## Testing

The project uses TestContainers for integration testing, ensuring that tests run against a real PostgreSQL database:

```java
@SpringBootTest
@Testcontainers
class EmployeeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private EmployeeRepository employeeRepository;
    
    // ...
}
```

## Using environment variables for user names, passwords, and URLs
This is a common setup. Spring Boot supports profiles and externalized config, so you can separate local/stage/prod and inject secrets via environment variables (including
from GitHub Actions secrets).

Typical approach:

- Use application.yaml for defaults.
- Add application-local.yaml, application-staging.yaml, application-prod.yaml.
- Activate with SPRING_PROFILES_ACTIVE=local|staging|prod.
- Reference secrets via env vars in YAML:
  password: ${DB_PASSWORD}

In GitHub Actions:

- Store secrets in repo/environment secrets (e.g., DB_PASSWORD, DB_USER).
- Export them in the workflow env: block.
- Spring Boot reads them automatically.
- 
### Usage
Usage:

- Local (default): just run the app
- Staging: SPRING_PROFILES_ACTIVE=staging POSTGRES_URL=... POSTGRES_USER=... POSTGRES_PASSWORD=... MYSQL_URL=... MYSQL_USER=... MYSQL_PASSWORD=...
- Prod: same env vars with SPRING_PROFILES_ACTIVE=prod

## Transactions

### NoteService Transaction Management
Here’s how NoteService works and what the @Transactional parameters do.

NoteService (src/main/java/dev/danvega/qbe/notes/NoteService.java) exposes three operations:

- findAll() → read‑only query
- search(...) → read‑only query with filters
- create(...) → write (insert)

Transactional annotations:

- Class level: @Transactional(transactionManager = "mysqlTransactionManager", readOnly = true)
  - Uses the MySQL transaction manager, not Postgres.
  - readOnly = true tells Spring/Hibernate these methods won’t write; it can optimize and prevents accidental writes in these methods.
- Method level on create(...):
  - @Transactional(transactionManager = "mysqlTransactionManager")
  - Overrides the class default to allow writes (no readOnly=true) so inserts/updates can occur.

Why this matters:

- You have two databases, so you must tell Spring which transaction manager to use.
- Reads default to MySQL and are read‑only.
- Writes explicitly use MySQL and allow changes.

### EmplyoyeeService Transaction Management
EmployeeService already has @Transactional(readOnly = true) at the class level, but it doesn’t specify a transactionManager because the Postgres one is marked @Primary in
PostgresJpaConfig. So Spring uses the primary transaction manager automatically.

NoteService must specify transactionManager = "mysqlTransactionManager" because that’s not the primary one.

So:

- EmployeeService → defaults to primary (Postgres)
- NoteService → explicitly targets MySQL

### spring.second-datasource
spring.second-datasource is not a Spring Boot standard. We invented that prefix so we could bind a second set of datasource properties without clashing with the default
spring.datasource.

Why it’s needed:

- Spring Boot only auto-binds one datasource by default (spring.datasource.*).
- For a second DB, you must choose your own prefix and bind it with @ConfigurationProperties.
- We used spring.second-datasource.* to keep it clear and grouped.

Where it’s used:

- In MysqlJpaConfig, the @ConfigurationProperties("spring.second-datasource") annotation tells Spring to bind those YAML properties into DataSourceProperties.
- Then we build the MySQL DataSource from that.

## Learn More

- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/reference/repositories/query-by-example.html)