package ro.uvt.fi.dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditAccountDecorator extends AccountDecorator {
    private final List<String> auditTrail = new ArrayList<>();

    public AuditAccountDecorator(Account wrapped) {
        super(wrapped);
    }

    @Override
    public void depose(double amount) {
        if (wrapped == null) {
            super.depose(amount);
            return;
        }
        wrapped.depose(amount);
        auditTrail.add("DEPOSIT " + amount + " -> balance=" + wrapped.getBalance());
    }

    @Override
    public void retrieve(double amount) {
        if (wrapped == null) {
            super.retrieve(amount);
            return;
        }
        wrapped.retrieve(amount);
        auditTrail.add("WITHDRAW " + amount + " -> balance=" + wrapped.getBalance());
    }

    public List<String> getAuditTrail() {
        return Collections.unmodifiableList(auditTrail);
    }
}