package ro.uvt.fi.dp;

// Issue 1 fix: EUR-specific logic is isolated here.
// Issue 6 fix: EurAccount does not implement Transfer — EUR accounts can't do local transfers.
// Issue 3 fix: flat interest rate stored in a named constant.
public class EurAccount extends Account {

    private static final double EUR_INTEREST_RATE = 0.01; // 1%

    EurAccount(String accountCode, double amount) {
        super(accountCode, amount, Account.TYPE.EUR);
    }

    @Override
    public double getInterest() {
        return EUR_INTEREST_RATE;
    }
}
