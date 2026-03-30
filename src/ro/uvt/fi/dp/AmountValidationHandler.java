package ro.uvt.fi.dp;

public class AmountValidationHandler extends TransactionHandler {
    @Override
    protected void validate(TransactionRequest request) {
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be strictly positive");
        }
    }
}
