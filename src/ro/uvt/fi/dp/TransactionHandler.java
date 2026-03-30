package ro.uvt.fi.dp;

public abstract class TransactionHandler {
    private TransactionHandler next;

    public TransactionHandler setNext(TransactionHandler next) {
        this.next = next;
        return next;
    }

    public final void handle(TransactionRequest request) {
        validate(request);
        if (next != null) {
            next.handle(request);
        }
    }

    protected abstract void validate(TransactionRequest request);
}
