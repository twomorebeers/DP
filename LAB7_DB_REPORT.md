# Lab 7 — Option C (Database + API)

## Chosen feature
Create a database for the application, link existing classes to the database, create tables for the entity classes, and expose an API to demo operations.

## Why this option
I chose option C to learn DB concepts (schema design + CRUD) and to make the bank system persistent.

## Architecture (simple)
API → Service (validation + command + chain) → Repository (JDBC) → Database (H2/PostgreSQL)

## Database schema (MVP)
- `banks(id, code, name, address)`
- `clients(id, bank_id, name, address, birth_day, premium)`
- `accounts(id, client_id, iban, currency, balance)`
- `operations(id, type, source_iban, destination_iban, amount, created_at, status)`

## API endpoints (demo)
- `POST /banks`
- `POST /clients/bank/{bankId}`
- `POST /accounts/client/{clientId}`
- `GET /accounts/{iban}`
- `POST /operations/deposit`
- `POST /operations/withdraw`
- `POST /operations/transfer`

## How it integrates with patterns
- **Chain of Responsibility** validates all operations.
- **Command** executes the operation and supports undo/redo in memory.
- **Decorator** can still wrap accounts before operations.

## Implementation steps (student plan)
1. Create DB schema and repositories.
2. Wire existing service logic to DB accounts.
3. Expose API endpoints for demo.
4. Add a few tests and sample requests.
