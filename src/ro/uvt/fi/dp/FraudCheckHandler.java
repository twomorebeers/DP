package ro.uvt.fi.dp;

public class FraudCheckHandler extends TransactionHandler {
    private static final double SUSPICIOUS_TRANSFER_THRESHOLD = 7_500.0;

    @Override
    protected void validate(TransactionRequest request) {
        if (request.getType() == OperationType.TRANSFER) {
            if (request.getSource() == null || request.getDestination() == null) {
                throw new IllegalArgumentException("Transfer requires source and destination accounts");
            }
            if (request.getSource().getAccountCode().equals(request.getDestination().getAccountCode())) {
                throw new IllegalStateException("Cannot transfer to the same account");
            }
            if (request.getAmount() >= SUSPICIOUS_TRANSFER_THRESHOLD) {
                throw new IllegalStateException("Transfer blocked by fraud check");
            }
        }
    }
}
