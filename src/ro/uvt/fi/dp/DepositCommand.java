package ro.uvt.fi.dp;

public class DepositCommand implements BankCommand {
    private final Account target;
    private final double amount;

    public DepositCommand(Account target, double amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void execute() {
        target.depose(amount);
    }

    @Override
    public void undo() {
        target.retrieve(amount);
    }

    @Override
    public String description() {
        return "Deposit " + amount + " to " + target.getAccountCode();
    }
}
