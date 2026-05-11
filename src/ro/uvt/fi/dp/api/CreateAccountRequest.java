package ro.uvt.fi.dp.api;

import ro.uvt.fi.dp.Account;

public class CreateAccountRequest {
    public String iban;
    public Account.TYPE currency;
    public double balance;
}
