package ro.uvt.fi.dp;

import java.util.Arrays;

import ro.uvt.fi.dp.Account.TYPE;

public class Client {
	public static final int MAX_ACCOUNTS_NO = 5;

	private String name;
	private String address;
	private Account accounts[];
	private int accountsNo = 0;

	public Client(String name, String address, TYPE type, String accountCode, double amount) {
		this.name = name;
		this.address = address;
		accounts = new Account[MAX_ACCOUNTS_NO];
		addAccount(type, accountCode, amount);
	}

	public void addAccount(TYPE type, String accountCode, double amount) {
		if (MAX_ACCOUNTS_NO > accountsNo)
			// Issue 2 fix: use the factory method instead of calling new Account() directly
			accounts[accountsNo++] = Account.of(accountCode, amount, type);
	}

	public Account getAccount(String accountCode) {
		for (int i = 0; i < accountsNo; i++) {
			if (accounts[i].getAccountCode().equals(accountCode)) {
				return accounts[i];
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "\n\tClient [name=" + name + ", address=" + address + ", accounts=" + Arrays.toString(accounts) + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
