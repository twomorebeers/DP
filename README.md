# Bank Management System (Design Patterns + DB + API)

## Overview
This project implements a bank management model with:
- Core entities: Bank, Client, Account (RON/EUR)
- Behavior patterns: Decorator, Command, Chain of Responsibility
- Database persistence and REST API (Lab 7 option C)

## Core domain (Lab 5)
- **Bank** manages clients.
- **Client** owns 1–5 accounts.
- **Account** has IBAN, balance, and currency (RON/EUR).
- Interest rules:
	- RON: 0.03 for balance < 500; 0.08 for balance ≥ 500
	- EUR: 0.01 regardless of balance

## Task 1 - Decorator pattern (for `Account`)

### Scenario A: promotional / premium interest rules
- Use case: same `Account` may receive temporary interest boosts (e.g., +2% for premium campaign).
- Implementation: [src/ro/uvt/fi/dp/BonusInterestDecorator.java](src/ro/uvt/fi/dp/BonusInterestDecorator.java), built on top of [src/ro/uvt/fi/dp/AccountDecorator.java](src/ro/uvt/fi/dp/AccountDecorator.java).
- Benefit: add behavior at runtime, per account instance, without modifying `RonAccount` / `EurAccount`.

### Scenario B: auditing/compliance trail
- Use case: some accounts need operation-level tracking for investigations or reports.
- Implementation: [src/ro/uvt/fi/dp/AuditAccountDecorator.java](src/ro/uvt/fi/dp/AuditAccountDecorator.java).
- Benefit: adds logging concern only where needed, preserving core account implementation.

### Why Decorator instead of a composition list of features
- A simple list of "features" mostly stores metadata; it does not naturally change account behavior (`getInterest()`, `getTotalAmount()`, `depose()`, `retrieve()`).
- Decorator keeps the same domain type (`Account`) and allows feature stacking in a transparent way.
- Existing services (like commands/validation) continue to work because decorated objects are still `Account` objects.

## Task 2 - Command pattern (undo-friendly operations)

### Target operations
- `deposit`
- `withdraw`
- `transfer`

These are good candidates because they are explicit user actions with reversible effects.

### Implementation
- Command abstraction: [src/ro/uvt/fi/dp/BankCommand.java](src/ro/uvt/fi/dp/BankCommand.java)
- Concrete commands:
	- [src/ro/uvt/fi/dp/DepositCommand.java](src/ro/uvt/fi/dp/DepositCommand.java)
	- [src/ro/uvt/fi/dp/WithdrawCommand.java](src/ro/uvt/fi/dp/WithdrawCommand.java)
	- [src/ro/uvt/fi/dp/TransferCommand.java](src/ro/uvt/fi/dp/TransferCommand.java)
- Invoker/history manager: [src/ro/uvt/fi/dp/BankCommandManager.java](src/ro/uvt/fi/dp/BankCommandManager.java)
- Facade/service entry point: [src/ro/uvt/fi/dp/BankOperationService.java](src/ro/uvt/fi/dp/BankOperationService.java)

### Expected outcome
- New operations can be added by introducing new command classes.
- Built-in undo/redo through command history stacks.
- Decouples operation request from execution details.

## Task 3 - Chain of Responsibility

Chosen pattern: **Chain of Responsibility** for request validation before command execution.

### Use case
Validate transaction requests through a pipeline so each rule is isolated and composable.

Pipeline in [src/ro/uvt/fi/dp/BankOperationService.java](src/ro/uvt/fi/dp/BankOperationService.java):

`AmountValidation -> LimitCheck -> FraudCheck -> BalanceCheck -> Approval`

### Core classes
- Base handler: [src/ro/uvt/fi/dp/TransactionHandler.java](src/ro/uvt/fi/dp/TransactionHandler.java)
- Request model: [src/ro/uvt/fi/dp/TransactionRequest.java](src/ro/uvt/fi/dp/TransactionRequest.java)
- Operation type: [src/ro/uvt/fi/dp/OperationType.java](src/ro/uvt/fi/dp/OperationType.java)
- Concrete handlers:
	- [src/ro/uvt/fi/dp/AmountValidationHandler.java](src/ro/uvt/fi/dp/AmountValidationHandler.java)
	- [src/ro/uvt/fi/dp/LimitCheckHandler.java](src/ro/uvt/fi/dp/LimitCheckHandler.java)
	- [src/ro/uvt/fi/dp/FraudCheckHandler.java](src/ro/uvt/fi/dp/FraudCheckHandler.java)
	- [src/ro/uvt/fi/dp/BalanceCheckHandler.java](src/ro/uvt/fi/dp/BalanceCheckHandler.java)
	- [src/ro/uvt/fi/dp/ApprovalHandler.java](src/ro/uvt/fi/dp/ApprovalHandler.java)

### Why appropriate
- Each validation rule has single responsibility.
- Rules can be reordered, replaced, or extended non-intrusively.
- Prevents monolithic if/else validation blocks.

## Quick verification
- Main demo: [src/ro/uvt/fi/dp/Test.java](src/ro/uvt/fi/dp/Test.java)
- Automated checks: [src/ro/uvt/fi/dp/AccountTest.java](src/ro/uvt/fi/dp/AccountTest.java)

## Lab 7 - Option C (Database + API)

### What was added
- Spring Boot entry point: [src/ro/uvt/fi/dp/BankApplication.java](src/ro/uvt/fi/dp/BankApplication.java)
- JDBC repositories: [src/ro/uvt/fi/dp/db](src/ro/uvt/fi/dp/db)
- REST controllers + DTOs: [src/ro/uvt/fi/dp/api](src/ro/uvt/fi/dp/api)
- DB schema and config: [src/main/resources/schema.sql](src/main/resources/schema.sql), [src/main/resources/application.yml](src/main/resources/application.yml)
- Report: [LAB7_DB_REPORT.md](LAB7_DB_REPORT.md)
- Insomnia import: [insomnia-export.json](insomnia-export.json)

### API endpoints (demo)
- `POST /banks`
- `GET /banks/{id}`
- `POST /clients/bank/{bankId}`
- `GET /clients/{id}`
- `POST /accounts/client/{clientId}`
- `GET /accounts/{iban}`
- `POST /operations/deposit`
- `POST /operations/withdraw`
- `POST /operations/transfer`

## Run the app
Use Maven to start the Spring Boot application:

	mvn spring-boot:run

The app starts on http://localhost:8080.

## Database behavior
- Default DB is H2 in **file mode** (persistent): `jdbc:h2:file:./data/bankdb`
- Tables are created from [src/main/resources/schema.sql](src/main/resources/schema.sql)
- Data persists across restarts until the file is deleted

### H2 console
- URL: http://localhost:8080/h2
- JDBC URL: `jdbc:h2:file:./data/bankdb`
- User: `sa`
- Password: *(empty)*

## Testing and proofs

### 1) Unit tests (logic + patterns)
- [src/ro/uvt/fi/dp/AccountTest.java](src/ro/uvt/fi/dp/AccountTest.java)

Run:

	mvn -q -DskipTests=false test

### 2) API demo (Insomnia)
Import [insomnia-export.json](insomnia-export.json) into Insomnia and run:
- Create bank → Create client → Create account → Deposit → Withdraw → Transfer → Get account

### 3) DB verification (terminal)
If the app is running, you can query H2 from the terminal using the H2 shell:

	java -cp ~/.m2/repository/com/h2database/h2/2.1.214/h2-2.1.214.jar org.h2.tools.Shell \
	  -url jdbc:h2:file:./data/bankdb -user sa

Then run:

	SELECT * FROM banks;
	SELECT * FROM clients;
	SELECT * FROM accounts;
	SELECT * FROM operations;

## Notes
- Re-running the same demo workflow will create duplicate records unless you change IDs/IBANs.
- Duplicate bank codes or IBANs will fail because they are unique in the DB.

