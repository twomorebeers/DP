package ro.uvt.fi.dp;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ro.uvt.fi.dp.db.AccountRecord;
import ro.uvt.fi.dp.db.AccountRepository;
import ro.uvt.fi.dp.db.BankRecord;
import ro.uvt.fi.dp.db.BankRepository;
import ro.uvt.fi.dp.db.ClientRecord;
import ro.uvt.fi.dp.db.ClientRepository;
import ro.uvt.fi.dp.db.OperationRecord;
import ro.uvt.fi.dp.db.OperationRepository;

@Service
public class BankDbService {
    private final BankRepository bankRepository;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final BankOperationService operationService = new BankOperationService();

    public BankDbService(BankRepository bankRepository,
                         ClientRepository clientRepository,
                         AccountRepository accountRepository,
                         OperationRepository operationRepository) {
        this.bankRepository = bankRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    public BankRecord createBank(String code, String name, String address) {
        if (code == null || code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank code is required");
        }
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank name is required");
        }
        return bankRepository.create(code, name, address);
    }

    public BankRecord getBankById(long id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found"));
    }

    public ClientRecord createClient(long bankId, String name, String address, java.time.LocalDate birthDay, boolean premium) {
        bankRepository.findById(bankId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found"));
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client name is required");
        }
        return clientRepository.create(bankId, name, address, birthDay, premium);
    }

    public ClientRecord getClientById(long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    }

    public AccountRecord createAccount(long clientId, String iban, Account.TYPE currency, double balance) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        if (iban == null || iban.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IBAN is required");
        }
        if (currency == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is required");
        }
        try {
            Account.of(iban, Math.max(0, balance), currency);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return accountRepository.create(clientId, iban, currency.name(), Math.max(0, balance));
    }

    public AccountRecord getAccount(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Transactional
    public OperationRecord deposit(String iban, double amount) {
        AccountRecord record = getAccount(iban);
        Account account = toAccount(record);
        operationService.deposit(account, amount);
        accountRepository.updateBalance(iban, account.getBalance());
        return operationRepository.create(OperationType.DEPOSIT.name(), iban, iban, amount, "SUCCESS");
    }

    @Transactional
    public OperationRecord withdraw(String iban, double amount) {
        AccountRecord record = getAccount(iban);
        Account account = toAccount(record);
        operationService.withdraw(account, amount);
        accountRepository.updateBalance(iban, account.getBalance());
        return operationRepository.create(OperationType.WITHDRAW.name(), iban, null, amount, "SUCCESS");
    }

    @Transactional
    public OperationRecord transfer(String sourceIban, String destinationIban, double amount) {
        AccountRecord sourceRecord = getAccount(sourceIban);
        AccountRecord destinationRecord = getAccount(destinationIban);
        Account source = toAccount(sourceRecord);
        Account destination = toAccount(destinationRecord);
        operationService.transfer(source, destination, amount);
        accountRepository.updateBalance(sourceIban, source.getBalance());
        accountRepository.updateBalance(destinationIban, destination.getBalance());
        return operationRepository.create(OperationType.TRANSFER.name(), sourceIban, destinationIban, amount, "SUCCESS");
    }

    private Account toAccount(AccountRecord record) {
        Account.TYPE type = Account.TYPE.valueOf(record.currency);
        return Account.of(record.iban, record.balance, type);
    }
}
