package ro.uvt.fi.dp;

public interface Operations {
    double getBalance();
    double getTotalAmount();

    double getInterest();

    void depose(double amount);

    void retrieve(double amount);
}
