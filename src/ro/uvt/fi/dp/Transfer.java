package ro.uvt.fi.dp;

// Issue 5 fix: added receiveFrom() with a name that makes the money direction obvious.
// Issue 6 fix: this interface is only implemented by RonAccount — not every account type.
public interface Transfer {

    // Receive `amount` from `source` into this account.
    // "this" account is the destination; `source` is debited.
    void receiveFrom(Account source, double amount);

    // Kept for backwards compatibility — delegates to receiveFrom() in implementations
    void transfer(Account source, double amount);
}
