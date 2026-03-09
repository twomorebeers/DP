package ro.uvt.fi.dp;

// Issue 1 fix: made Account abstract so subclasses (RonAccount, EurAccount) handle
//              type-specific logic via polymorphism instead of if/else on TYPE everywhere.
// Issue 2 fix: added Account.of() static factory method that validates input before
//              delegating to the protected constructor.
// Issue 6 fix: Account no longer implements Transfer — only RonAccount will do that,
//              because EUR accounts don't support transfers.
// Issue 7 fix: accountCode is validated in the constructor with a basic IBAN-style regex.
// Issue 8 fix: toString() now delegates to AccountFormatter so the domain class
//              doesn't mix business logic with presentation.
public abstract class Account implements Operations {

    // Supported currency types — each maps to a concrete subclass
    public static enum TYPE {
        EUR, RON
    }

    // IBAN regex: 2 letters + 2 digits + up to 30 alphanumeric chars (simplified)
    private static final String IBAN_REGEX = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$";

    // Issue 3 fix: named the field clearly; actual interest thresholds live in RonAccount
    String accountCode;
    double amount = 0;
    Account.TYPE type;

    // Issue 7: validate IBAN format right here — fail fast is better than failing later
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

    // Issue 2: factory method — callers use this instead of `new Account(...)` directly.
    // It validates and then picks the right subclass automatically.
    public static Account of(String accountCode, double amount, Account.TYPE type) {
        if (type == Account.TYPE.RON) {
            return new RonAccount(accountCode, amount);
        } else {
            return new EurAccount(accountCode, amount);
        }
    }

    // Issue 4 fix: separated the two concerns — getBalance() just returns the raw amount,
    // getTotalAmount() computes the projection. Much easier to test each in isolation.
    public double getBalance() {
        return amount;
    }

    // getTotalAmount() = balance + projected interest; still part of Operations contract
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

    // Issue 1: toString() now delegates to the formatter — no type checks here
    @Override
    public String toString() {
        return AccountFormatter.format(this);
    }

    // Each subclass knows its own interest rules (Issue 1 / DRY fix)
    @Override
    public abstract double getInterest();
}
