package ro.uvt.fi.dp;

public class RonAccount extends Account implements Transfer {
    private static final double LOW_BALANCE_THRESHOLD = 500.0;
    private static final double LOW_INTEREST_RATE = 0.03;   // 3%
    private static final double HIGH_INTEREST_RATE = 0.08;  // 8%

    RonAccount(String accountCode, double amount) {
        super(accountCode, amount, Account.TYPE.RON);
    }

    @Override
    public double getInterest() {
        if (amount < LOW_BALANCE_THRESHOLD) {
            return LOW_INTEREST_RATE;
        }
        return HIGH_INTEREST_RATE;
    }

    @Override
    public void receiveFrom(Account source, double amount) {
        source.retrieve(amount);
        this.depose(amount);
    }

    @Override
    public void transfer(Account source, double amount) {
        receiveFrom(source, amount);
    }
}
