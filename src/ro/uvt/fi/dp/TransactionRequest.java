package ro.uvt.fi.dp;

public class TransactionRequest {
    private final OperationType type;
    private final Account source;
    private final Account destination;
    private final double amount;

    public TransactionRequest(OperationType type, Account source, Account destination, double amount) {
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public OperationType getType() {
        return type;
    }

    public Account getSource() {
        return source;
    }

    public Account getDestination() {
        return destination;
    }

    public double getAmount() {
        return amount;
    }
}
