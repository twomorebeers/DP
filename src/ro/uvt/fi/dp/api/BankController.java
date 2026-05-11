package ro.uvt.fi.dp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.uvt.fi.dp.BankDbService;
import ro.uvt.fi.dp.db.BankRecord;

@RestController
@RequestMapping("/banks")
public class BankController {
    private final BankDbService service;

    public BankController(BankDbService service) {
        this.service = service;
    }

    @PostMapping
    public BankResponse createBank(@RequestBody CreateBankRequest request) {
        BankRecord record = service.createBank(request.code, request.name, request.address);
        return toResponse(record);
    }

    @GetMapping("/{id}")
    public BankResponse getBank(@PathVariable long id) {
        BankRecord record = service.getBankById(id);
        return toResponse(record);
    }

    private BankResponse toResponse(BankRecord record) {
        BankResponse response = new BankResponse();
        response.id = record.id;
        response.code = record.code;
        response.name = record.name;
        response.address = record.address;
        return response;
    }
}
