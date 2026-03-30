package ro.uvt.fi.dp;

public class BankOperationService {
    private static final double DEFAULT_MAX_OPERATION_AMOUNT = 10_000.0;

    private final BankCommandManager commandManager = new BankCommandManager();
    private final TransactionHandler validationChain;

    public BankOperationService() {
        validationChain = buildDefaultChain();
    }

    private TransactionHandler buildDefaultChain() {
        TransactionHandler amountValidation = new AmountValidationHandler();
        TransactionHandler limitCheck = new LimitCheckHandler(DEFAULT_MAX_OPERATION_AMOUNT);
        TransactionHandler fraudCheck = new FraudCheckHandler();
        TransactionHandler balanceCheck = new BalanceCheckHandler();
        TransactionHandler approval = new ApprovalHandler();

        amountValidation
                .setNext(limitCheck)
                .setNext(fraudCheck)
                .setNext(balanceCheck)
                .setNext(approval);
        return amountValidation;
    }

    public void deposit(Account target, double amount) {
        if (target == null) {
            throw new IllegalArgumentException("Target account is required");
        }
        validate(new TransactionRequest(OperationType.DEPOSIT, target, target, amount));
        commandManager.execute(new DepositCommand(target, amount));
    }

    public void withdraw(Account source, double amount) {
        if (source == null) {
            throw new IllegalArgumentException("Source account is required");
        }
        validate(new TransactionRequest(OperationType.WITHDRAW, source, null, amount));
        commandManager.execute(new WithdrawCommand(source, amount));
    }

    public void transfer(Account source, Account destination, double amount) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Transfer requires source and destination accounts");
        }
        validate(new TransactionRequest(OperationType.TRANSFER, source, destination, amount));
        commandManager.execute(new TransferCommand(source, destination, amount));
    }

    public void undoLast() {
        commandManager.undoLast();
    }

    public void redoLast() {
        commandManager.redoLast();
    }

    private void validate(TransactionRequest request) {
        validationChain.handle(request);
    }
}
