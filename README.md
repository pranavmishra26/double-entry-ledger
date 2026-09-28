# Double Entry Ledger System

A backend service that models financial transactions using the **double-entry bookkeeping** approach.

The project is built with Java, Spring Boot, Spring Data JPA and PostgreSQL. The main focus is on modeling accounts and ledger entries, enforcing transaction rules, and keeping the database changes for a transaction consistent.

## What it does

The system supports:

* Creating financial accounts
* Retrieving account information
* Creating debit/credit transactions between accounts
* Checking an account's current balance
* Viewing an account's ledger history
* Validating transaction inputs
* Handling invalid requests through centralized exception handling

Each transaction creates two ledger entries:

```text
Debit Account  -> DEBIT  -> Amount
Credit Account -> CREDIT -> Amount
```

Both entries belong to the same ledger transaction.

## Tech Stack

* **Java 25**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA / Hibernate**
* **PostgreSQL**
* **Maven**
* **REST API**
* **IntelliJ IDEA**

## Architecture

The application follows a layered backend structure:

```text
Client
   |
   v
Controller Layer
   |
   v
Service Layer
   |
   v
Repository Layer
   |
   v
PostgreSQL
```

### Controller Layer

Handles HTTP requests and responses.

The controllers are responsible for receiving request data, calling the appropriate service, and converting the result into API responses.

### Service Layer

Contains the main business logic.

For example, before creating a ledger transaction, the service verifies that:

* Debit and credit accounts are different
* The transaction amount is greater than zero
* Both accounts exist
* Both accounts are active

The transaction creation method is also marked with `@Transactional` so the related database operations are handled as one transaction.

### Repository Layer

Uses Spring Data JPA repositories to interact with PostgreSQL.

The ledger entry repository also contains the query used to calculate an account's balance from its debit and credit entries.

## Data Model

The core of the system consists of three entities.

### Account

Represents an account in the ledger.

Each account contains:

* UUID
* Account number
* Name
* Account type
* Active/inactive status

Supported account types are:

```text
ASSET
LIABILITY
EQUITY
REVENUE
EXPENSE
```

Account numbers are unique at the database level.

### LedgerTransaction

Represents a financial transaction.

A transaction contains:

* UUID
* Creation timestamp
* A collection of ledger entries

A transaction owns its ledger entries, so the entries are persisted together with the transaction.

### LedgerEntry

Represents one side of a financial transaction.

Each entry contains:

* UUID
* Account
* Entry type (`DEBIT` or `CREDIT`)
* Amount
* Parent ledger transaction

A normal transfer therefore creates two entries belonging to the same transaction.

## Transaction Flow

For example, if Account A transfers ₹500 to Account B:

```text
                Ledger Transaction
                       |
             +---------+---------+
             |                   |
             v                   v
       Account A             Account B
         DEBIT                 CREDIT
         ₹500                   ₹500
```

The transaction service creates both entries and persists them together.

The service rejects transactions where:

* The debit and credit accounts are the same
* The amount is null or less than or equal to zero
* The debit account does not exist
* The credit account does not exist
* Either account is inactive

## Balance Calculation

The account balance is not stored as a separate mutable field.

Instead, it is calculated from the account's ledger entries:

```text
DEBIT  -> + amount
CREDIT -> - amount
```

The calculation is performed at the repository level using a JPQL query.

This keeps the ledger entries as the underlying record of the account's financial activity.

## REST API

### Account APIs

| Method | Endpoint                 | Purpose                        |
| ------ | ------------------------ | ------------------------------ |
| `POST` | `/accounts`              | Create an account              |
| `GET`  | `/accounts/{id}`         | Get account details            |
| `GET`  | `/accounts/{id}/balance` | Get calculated account balance |
| `GET`  | `/accounts/{id}/ledger`  | Get account ledger history     |

### Transaction API

| Method | Endpoint        | Purpose                           |
| ------ | --------------- | --------------------------------- |
| `POST` | `/transactions` | Create a debit/credit transaction |

Example transaction request:

```json
{
  "debitAccountId": "account-a-uuid",
  "creditAccountId": "account-b-uuid",
  "amount": 500.00
}
```

The response contains the created transaction along with its ledger entries.

## Error Handling

The application uses a centralized `@RestControllerAdvice` for exception handling.

It handles cases such as:

* Account not found
* Duplicate account
* Inactive account
* Invalid transaction
* Other invalid requests

The API returns appropriate HTTP status codes along with structured error information.

For example:

```text
404 -> Account Not Found
409 -> Duplicate Account
400 -> Invalid Request / Transaction
```

## Database

The application uses PostgreSQL with Hibernate/JPA for persistence.

The main tables are:

```text
accounts
ledger_transactions
ledger_entries
```

The relationships between these tables model the relationship between accounts, transactions, and their individual ledger entries.

## What I focused on

The main focus of this project was understanding how a backend financial system should handle business rules and data consistency rather than building a basic CRUD application.

Some of the areas I worked with were:

* Layered backend architecture
* REST API design
* Service-layer business logic
* JPA entity relationships
* PostgreSQL persistence
* Double-entry transaction modeling
* Balance calculation from ledger entries
* Transaction management with `@Transactional`
* Centralized exception handling
* DTO-based API responses

## Current Status

The core ledger functionality is implemented:

```text
Create Account
      ↓
Create Transaction
      ↓
Create Debit + Credit Entries
      ↓
Persist Transaction
      ↓
Calculate Account Balance
      ↓
View Ledger History
```

The project currently focuses on the core ledger functionality and backend architecture. More advanced production-level concerns can be added as the project evolves.

## Future Improvements

Some areas I plan to explore further:

* Idempotency for transaction requests
* Concurrency control and database locking
* More comprehensive automated tests
* Auditability and immutable ledger handling
* Observability and monitoring
* Docker-based deployment
* CI/CD

These are intentionally listed as future improvements rather than features of the current implementation.

## Project Goal

This project started as a way to build something more meaningful than a basic CRUD application.

The main objective is to understand how a backend system can model financial transactions, enforce business rules, interact with a relational database, and keep related operations consistent.

The project will continue to evolve toward a more production-oriented ledger system as I work on areas such as concurrency, idempotency, testing, observability, and deployment.

