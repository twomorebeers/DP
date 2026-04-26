package ro.uvt.fi.dp;

public class BonusInterestDecorator extends AccountDecorator {
    private final double bonusInterestRate;

    public BonusInterestDecorator(Account wrapped, double bonusInterestRate) {
        super(wrapped);
        if (bonusInterestRate < 0) {
            throw new IllegalArgumentException("Bonus interest must be non-negative");
        }
        this.bonusInterestRate = bonusInterestRate;
    }

    @Override
    public double getInterest() {
        return wrapped.getInterest() + bonusInterestRate;
    }

    @Override
    public double getTotalAmount() {
        double balance = getBalance();
        return balance + balance * getInterest();
    }
}