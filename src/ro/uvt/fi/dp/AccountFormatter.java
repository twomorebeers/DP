package ro.uvt.fi.dp;

public class AccountFormatter {
    private AccountFormatter() {}

    public static String format(Account account) {
        String currencyLabel = (account.type == Account.TYPE.RON) ? "RON" : "EUR";
        return "Account " + currencyLabel + ": code=" + account.accountCode
                + ", balance=" + account.getBalance()
                + ", totalWithInterest=" + String.format("%.2f", account.getTotalAmount());
    }
}
