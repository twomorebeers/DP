package ro.uvt.fi.dp.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.uvt.fi.dp.BankDbService;
import ro.uvt.fi.dp.db.OperationRecord;

@RestController
@RequestMapping("/operations")
public class OperationController {
    private final BankDbService service;

    public OperationController(BankDbService service) {
        this.service = service;
    }

    @PostMapping("/deposit")
    public OperationResponse deposit(@RequestBody OperationRequest request) {
        OperationRecord record = service.deposit(request.iban, request.amount);
        return toResponse(record);
    }

    @PostMapping("/withdraw")
    public OperationResponse withdraw(@RequestBody OperationRequest request) {
        OperationRecord record = service.withdraw(request.iban, request.amount);
        return toResponse(record);
    }

    @PostMapping("/transfer")
    public OperationResponse transfer(@RequestBody TransferRequest request) {
        OperationRecord record = service.transfer(request.sourceIban, request.destinationIban, request.amount);
        return toResponse(record);
    }

    private OperationResponse toResponse(OperationRecord record) {
        OperationResponse response = new OperationResponse();
        response.id = record.id;
        response.type = record.type;
        response.sourceIban = record.sourceIban;
        response.destinationIban = record.destinationIban;
        response.amount = record.amount;
        response.status = record.status;
        response.createdAt = record.createdAt;
        return response;
    }
}
