package ro.uvt.fi.dp.db;

import java.time.LocalDateTime;

public class OperationRecord {
    public Long id;
    public String type;
    public String sourceIban;
    public String destinationIban;
    public double amount;
    public String status;
    public LocalDateTime createdAt;
}
