package ro.uvt.fi.dp;

import java.util.HashMap;
import java.util.Map;

public final class BankRegistry {

    private static final BankRegistry INSTANCE = new BankRegistry();
    private final Map<String, Bank> banks = new HashMap<>();

    private BankRegistry() {
    }

    public static BankRegistry getInstance() {
        return INSTANCE;
    }

    public synchronized void register(Bank bank) {
        if (bank == null || bank.getBankCode() == null) {
            return;
        }
        banks.put(bank.getBankCode(), bank);
    }

    public synchronized Bank get(String bankCode) {
        return banks.get(bankCode);
    }

    public synchronized int size() {
        return banks.size();
    }
}
