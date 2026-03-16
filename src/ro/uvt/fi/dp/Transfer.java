package ro.uvt.fi.dp;

public interface Transfer {
    void receiveFrom(Account source, double amount);
    void transfer(Account source, double amount);
}
