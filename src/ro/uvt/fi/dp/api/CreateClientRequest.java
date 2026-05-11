package ro.uvt.fi.dp.api;

import java.time.LocalDate;

public class CreateClientRequest {
    public String name;
    public String address;
    public LocalDate birthDay;
    public boolean premium;
}
