package ro.uvt.fi.dp;

public class LimitCheckHandler extends TransactionHandler {
    private final double maxAmount;

    public LimitCheckHandler(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    protected void validate(TransactionRequest request) {
        if (request.getAmount() > maxAmount) {
            throw new IllegalStateException("Operation exceeds per-transaction limit of " + maxAmount);
        }
    }
}
