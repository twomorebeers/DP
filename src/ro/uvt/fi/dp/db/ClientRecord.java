package ro.uvt.fi.dp.db;

import java.time.LocalDate;

public class ClientRecord {
    public Long id;
    public Long bankId;
    public String name;
    public String address;
    public LocalDate birthDay;
    public boolean premium;
}
