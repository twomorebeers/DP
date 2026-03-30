package ro.uvt.fi.dp;

public class TransferCommand implements BankCommand {
    private final Account source;
    private final Account destination;
    private final double amount;

    public TransferCommand(Account source, Account destination, double amount) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    @Override
    public void execute() {
        source.retrieve(amount);
        destination.depose(amount);
    }

    @Override
    public void undo() {
        destination.retrieve(amount);
        source.depose(amount);
    }

    @Override
    public String description() {
        return "Transfer " + amount + " from " + source.getAccountCode() + " to "
                + destination.getAccountCode();
    }
}
