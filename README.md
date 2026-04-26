# DP - Lab 5 (Decorator, Command, Chain of Responsibility)

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
