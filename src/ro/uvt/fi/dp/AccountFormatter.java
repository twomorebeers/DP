package ro.uvt.fi.dp;

// Issue 8 fix: formatting logic extracted out of Account.
// The domain model (Account) shouldn't care how it's presented — that's this class's job.
public class AccountFormatter {

    private AccountFormatter() {}

    public static String format(Account account) {
        String currencyLabel = (account.type == Account.TYPE.RON) ? "RON" : "EUR";
        return "Account " + currencyLabel + ": code=" + account.accountCode
                + ", balance=" + account.getBalance()
                + ", totalWithInterest=" + String.format("%.2f", account.getTotalAmount());
    }
}
