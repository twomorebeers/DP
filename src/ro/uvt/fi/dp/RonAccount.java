package ro.uvt.fi.dp;

// Issue 1 fix: RON-specific logic lives here — no more if/else on TYPE.
// Issue 6 fix: only RonAccount implements Transfer because EUR accounts don't support it.
// Issue 5 fix: transfer() is renamed to receiveFrom() to make it crystal clear
//              that money flows INTO this account FROM the other one.
// Issue 3 fix: magic numbers replaced with named constants with explanations.
public class RonAccount extends Account implements Transfer {

    // Below this threshold the interest rate is lower (regulatory rule)
    private static final double LOW_BALANCE_THRESHOLD = 500.0;

    // Interest rate for balances below the threshold
    private static final double LOW_INTEREST_RATE = 0.03;   // 3%

    // Better rate once you save more than the threshold
    private static final double HIGH_INTEREST_RATE = 0.08;  // 8%

    // Package-private so Account.of() can reach it, but nobody else needs to call this directly
    RonAccount(String accountCode, double amount) {
        super(accountCode, amount, Account.TYPE.RON);
    }

    // Two-tier interest: save more, earn more
    @Override
    public double getInterest() {
        if (amount < LOW_BALANCE_THRESHOLD) {
            return LOW_INTEREST_RATE;
        }
        return HIGH_INTEREST_RATE;
    }

    // Issue 5: this method takes money FROM `source` and deposits it INTO this account.
    // The name now reflects that clearly (was misleadingly called transfer() before).
    @Override
    public void receiveFrom(Account source, double amount) {
        source.retrieve(amount);
        this.depose(amount);
    }

    // Issue 5 note: kept the old transfer() signature too so existing code doesn't break,
    // but it now delegates to receiveFrom() with the corrected direction.
    @Override
    public void transfer(Account source, double amount) {
        receiveFrom(source, amount);
    }
}
