package ro.uvt.fi.dp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.uvt.fi.dp.BankDbService;
import ro.uvt.fi.dp.db.AccountRecord;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final BankDbService service;

    public AccountController(BankDbService service) {
        this.service = service;
    }

    @PostMapping("/client/{clientId}")
    public AccountResponse createAccount(@PathVariable long clientId, @RequestBody CreateAccountRequest request) {
        AccountRecord record = service.createAccount(clientId, request.iban, request.currency, request.balance);
        return toResponse(record);
    }

    @GetMapping("/{iban}")
    public AccountResponse getAccount(@PathVariable String iban) {
        AccountRecord record = service.getAccount(iban);
        return toResponse(record);
    }

    private AccountResponse toResponse(AccountRecord record) {
        AccountResponse response = new AccountResponse();
        response.id = record.id;
        response.clientId = record.clientId;
        response.iban = record.iban;
        response.currency = record.currency;
        response.balance = record.balance;
        return response;
    }
}
