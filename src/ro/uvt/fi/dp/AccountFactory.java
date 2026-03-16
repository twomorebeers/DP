package ro.uvt.fi.dp;

public final class AccountFactory {

    private AccountFactory() {
    }

    public static Account create(String accountCode, double amount, Account.TYPE type) {
        return type == Account.TYPE.RON
                ? new RonAccount(accountCode, amount)
                : new EurAccount(accountCode, amount);
    }
}
