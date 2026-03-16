package ro.uvt.fi.dp;

import java.util.Arrays;
import java.time.LocalDate;

import ro.uvt.fi.dp.Account.TYPE;

public class Client {
	public static final int MAX_ACCOUNTS_NO = 5;

	private String name;
	private String address;
	private LocalDate birthDay;
	private boolean premium;
	private Account accounts[];
	private int accountsNo = 0;

	public Client(String name, String address, TYPE type, String accountCode, double amount) {
		this.name = name;
		this.address = address;
		accounts = new Account[MAX_ACCOUNTS_NO];
		addAccount(type, accountCode, amount);
	}

	private Client(Builder builder) {
		this.name = builder.name;
		this.address = builder.address;
		this.birthDay = builder.birthDay;
		this.premium = builder.premium;
		accounts = new Account[MAX_ACCOUNTS_NO];
		if (builder.initialAccountType != null && builder.initialAccountCode != null) {
			addAccount(builder.initialAccountType, builder.initialAccountCode, builder.initialAmount);
		}
	}

	public static Builder builder(String name) {
		return new Builder(name);
	}

	public void addAccount(TYPE type, String accountCode, double amount) {
		if (MAX_ACCOUNTS_NO > accountsNo)
			accounts[accountsNo++] = AccountFactory.create(accountCode, amount, type);
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
		return "\n\tClient [name=" + name + ", address=" + address + ", birthDay=" + birthDay + ", premium="
				+ premium + ", accounts=" + Arrays.toString(accounts) + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static class Builder {
		private final String name;
		private String address;
		private LocalDate birthDay;
		private boolean premium;
		private TYPE initialAccountType;
		private String initialAccountCode;
		private double initialAmount;

		private Builder(String name) {
			if (name == null || name.isBlank()) {
				throw new IllegalArgumentException("Client name is required");
			}
			this.name = name;
		}

		public Builder address(String address) {
			this.address = address;
			return this;
		}

		public Builder birthDay(LocalDate birthDay) {
			this.birthDay = birthDay;
			return this;
		}

		public Builder premium(boolean premium) {
			this.premium = premium;
			return this;
		}

		public Builder initialAccount(TYPE type, String accountCode, double amount) {
			this.initialAccountType = type;
			this.initialAccountCode = accountCode;
			this.initialAmount = amount;
			return this;
		}

		public Client build() {
			return new Client(this);
		}
	}
}
