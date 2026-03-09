package ro.uvt.fi.dp;

// Issue 4 fix: added getBalance() so callers can get the raw balance independently
// from getTotalAmount() (which already includes projected interest).
// This makes each method have a single, clear responsibility.
public interface Operations {

    // Raw balance — no interest, no projection, just what's currently in the account
    double getBalance();

    // Projected total = balance + interest; useful for reports/summaries
    double getTotalAmount();

    double getInterest();

    void depose(double amount);

    void retrieve(double amount);
}
