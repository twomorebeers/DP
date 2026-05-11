package ro.uvt.fi.dp.api;

import java.time.LocalDateTime;

public class OperationResponse {
    public Long id;
    public String type;
    public String sourceIban;
    public String destinationIban;
    public double amount;
    public String status;
    public LocalDateTime createdAt;
}
