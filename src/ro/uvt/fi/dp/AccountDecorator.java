package ro.uvt.fi.dp;

public abstract class AccountDecorator extends Account {
    protected final Account wrapped;

    protected AccountDecorator(Account wrapped) {
        super(wrapped.getAccountCode(), 0, wrapped.type);
        this.wrapped = wrapped;
    }

    public Account getWrapped() {
        return wrapped;
    }

    @Override
    public double getBalance() {
        return wrapped.getBalance();
    }

    @Override
    public void depose(double amount) {
        if (wrapped == null) {
            this.amount += amount;
            return;
        }
        wrapped.depose(amount);
    }

    @Override
    public void retrieve(double amount) {
        if (wrapped == null) {
            this.amount -= amount;
            return;
        }
        wrapped.retrieve(amount);
    }

    @Override
    public double getTotalAmount() {
        return wrapped.getTotalAmount();
    }

    @Override
    public double getInterest() {
        return wrapped.getInterest();
    }
}