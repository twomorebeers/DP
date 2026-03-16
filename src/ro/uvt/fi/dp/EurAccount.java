package ro.uvt.fi.dp;

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
