# Double Entry Ledger

## 1. Project Overview
- What the project is
- What problem it solves
- Short explanation of double-entry accounting

## 2. Key Features
- Account creation and management
- Account types
- Debit/credit transactions
- Ledger entries
- Balance calculation
- Transaction history
- Validation
- Database transactions

## 3. Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- REST API

## 4. Architecture
- Client → Controller → Service → Repository → PostgreSQL
- Brief explanation of each layer

## 5. Domain Model
- Account
- LedgerTransaction
- LedgerEntry
- AccountType
- EntryType
- Explain how these entities relate to each other

## 6. Double-Entry Model
Show a simple example:

DEBIT  ₹500 → Cash Account
CREDIT ₹500 → Revenue Account

Explain that every transaction records both sides.

## 7. API Endpoints
Document the important endpoints:

POST   /accounts
GET    /accounts/{id}
GET    /accounts/{id}/balance
GET    /accounts/{id}/ledger
POST   /transactions

Include example request/response JSON for the main endpoints.

## 8. Database
- PostgreSQL
- Main tables
- Basic relationship between the tables

## 9. Project Structure
Show the controller / service / repository / model package structure.

## 10. Setup & Installation
- Prerequisites
- Create PostgreSQL database
- Configure environment variables
- Run the application
- Application URL

## 11. Example Workflow
Show a simple end-to-end flow:

1. Create two accounts
2. Create a debit/credit transaction
3. Retrieve the transaction
4. Check account balance
5. Check ledger history

## 12. Current Scope
Clearly state what is currently implemented.

## 13. Future Improvements
Only list things you genuinely plan to add, such as:
- Idempotency
- Concurrency control
- Database locking
- Automated tests
- Audit trail
- Docker
- Observability

## 14. What I Learned / Engineering Concepts
Briefly mention:
- Layered architecture
- REST API design
- JPA/Hibernate
- PostgreSQL persistence
- Transaction management
- Domain modelling
- Git/GitHub workflow
