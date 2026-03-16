package ro.uvt.fi.dp;

import java.util.Arrays;
import java.time.LocalDate;

public class Bank {

	private final static int MAX_CLIENTS_NUMBER = 100;
	private Client clients[];
	private int clientsNumber;
	private String bankCode = null;
	private String address;
	private LocalDate establishedOn;

	public Bank(String codBanca) {
		this.bankCode = codBanca;
		clients = new Client[MAX_CLIENTS_NUMBER];
	}

	private Bank(Builder builder) {
		this.bankCode = builder.bankCode;
		this.address = builder.address;
		this.establishedOn = builder.establishedOn;
		clients = new Client[MAX_CLIENTS_NUMBER];
	}

	public static Builder builder(String bankCode) {
		return new Builder(bankCode);
	}

	public void addClient(Client c) {
		clients[clientsNumber++] = c;
	}

	
	public Client getClient(String nume) {
		for (int i = 0; i < clientsNumber; i++) {
			if (clients[i].getName().equals(nume)) {
				return clients[i];
			}
		}
		return null;
	}

	public String getBankCode() {
		return bankCode;
	}
	
	@Override
	public String toString() {
		return "Bank [code=" + bankCode + ", address=" + address + ", establishedOn=" + establishedOn
				+ ", clients=" + Arrays.toString(clients) + "]";
	}

	public static class Builder {
		private final String bankCode;
		private String address;
		private LocalDate establishedOn;

		private Builder(String bankCode) {
			this.bankCode = bankCode;
		}

		public Builder address(String address) {
			this.address = address;
			return this;
		}

		public Builder establishedOn(LocalDate establishedOn) {
			this.establishedOn = establishedOn;
			return this;
		}

		public Bank build() {
			return new Bank(this);
		}
	}

}
