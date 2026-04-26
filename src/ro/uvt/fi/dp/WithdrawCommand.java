package ro.uvt.fi.dp;

public class WithdrawCommand implements BankCommand {
    private final Account source;
    private final double amount;

    public WithdrawCommand(Account source, double amount) {
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void execute() {
        source.retrieve(amount);
    }

    @Override
    public void undo() {
        source.depose(amount);
    }

    @Override
    public String description() {
        return "Withdraw " + amount + " from " + source.getAccountCode();
    }
}
