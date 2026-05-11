package ro.uvt.fi.dp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.uvt.fi.dp.BankDbService;
import ro.uvt.fi.dp.db.ClientRecord;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final BankDbService service;

    public ClientController(BankDbService service) {
        this.service = service;
    }

    @PostMapping("/bank/{bankId}")
    public ClientResponse createClient(@PathVariable long bankId, @RequestBody CreateClientRequest request) {
        ClientRecord record = service.createClient(bankId, request.name, request.address, request.birthDay, request.premium);
        return toResponse(record);
    }

    @GetMapping("/{id}")
    public ClientResponse getClient(@PathVariable long id) {
        ClientRecord record = service.getClientById(id);
        return toResponse(record);
    }

    private ClientResponse toResponse(ClientRecord record) {
        ClientResponse response = new ClientResponse();
        response.id = record.id;
        response.bankId = record.bankId;
        response.name = record.name;
        response.address = record.address;
        response.birthDay = record.birthDay;
        response.premium = record.premium;
        return response;
    }
}
