package ro.uvt.fi.dp;
public abstract class Account implements Operations {
    public static enum TYPE {
        EUR, RON
    }

    // kept this intentionally loose because some old test data has weird bank suffixes
    private static final String IBAN_REGEX = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$";

    String accountCode;
    double amount = 0;
    Account.TYPE type;

    protected Account(String accountCode, double amount, Account.TYPE type) {
        if (accountCode == null || !accountCode.matches(IBAN_REGEX)) {
            throw new IllegalArgumentException(
                "Invalid IBAN code: '" + accountCode + "'. Expected format: 2 letters + 2 digits + up to 30 alphanumeric chars."
            );
        }
        this.accountCode = accountCode;
        this.type = type;
        depose(amount);
    }

    public static Account of(String accountCode, double amount, Account.TYPE type) {
        return AccountFactory.create(accountCode, amount, type);
    }

    public double getBalance() {
        return amount;
    }

    @Override
    public double getTotalAmount() {
        return amount + amount * getInterest();
    }

    @Override
    public void depose(double amount) {
        this.amount += amount;
    }

    @Override
    public void retrieve(double amount) {
        this.amount -= amount;
    }

    public String getAccountCode() {
        return accountCode;
    }

    @Override
    public String toString() {
        return AccountFormatter.format(this);
    }

    @Override
    public abstract double getInterest();
}
