package ro.uvt.fi.dp;

public class BalanceCheckHandler extends TransactionHandler {
    @Override
    protected void validate(TransactionRequest request) {
        if (request.getType() == OperationType.WITHDRAW || request.getType() == OperationType.TRANSFER) {
            Account source = request.getSource();
            if (source == null) {
                throw new IllegalArgumentException("Operation requires a source account");
            }
            if (source.getBalance() < request.getAmount()) {
                throw new IllegalStateException("Insufficient balance in source account");
            }
        }
    }
}
