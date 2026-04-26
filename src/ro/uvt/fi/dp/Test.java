package ro.uvt.fi.dp;

import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {
    Bank bcr = Bank.builder("BCR Bank")
        .address("Timisoara")
        .establishedOn(LocalDate.of(1991, 10, 1))
        .build();

    Client cl1 = Client.builder("Ionescu Ion")
        .address("Timisoara")
        .birthDay(LocalDate.of(1994, 2, 11))
        .premium(true)
        .initialAccount(Account.TYPE.EUR, "EU02BCRF0000000000124", 200.9)
        .build();

        bcr.addClient(cl1);
        cl1.addAccount(Account.TYPE.RON, "RO49BCRF0000001234567", 400);

    Client cl2 = Client.builder("Marinescu Marin")
        .address("Timisoara")
        .initialAccount(Account.TYPE.RON, "RO49BCRF0000001260000", 100)
        .build();

        bcr.addClient(cl2);

    BankRegistry registry = BankRegistry.getInstance();
    registry.register(bcr);

        System.out.println("=== Initial state ===");
        System.out.println(bcr);

    Bank cec = Bank.builder("CEC Bank")
        .address("Brasov")
        .build();

    Client clientCEC = Client.builder("Vasilescu Vasile")
        .address("Brasov")
        .initialAccount(Account.TYPE.EUR, "EU02CECF0000000000128", 700)
        .build();

        cec.addClient(clientCEC);
    registry.register(cec);
        System.out.println(cec);
    System.out.println("Registry size: " + registry.size());

        Client cl = bcr.getClient("Marinescu Marin");
        if (cl != null) {
            cl.getAccount("RO49BCRF0000001260000").depose(400);
            System.out.println("\nAfter deposit of 400:");
            System.out.println(cl);

            cl.getAccount("RO49BCRF0000001260000").retrieve(67);
            System.out.println("\nAfter retrieval of 67:");
            System.out.println(cl);
        }

        Account ronSource = cl.getAccount("RO49BCRF0000001260000");
        Account ronTarget = bcr.getClient("Ionescu Ion").getAccount("RO49BCRF0000001234567");

        if (ronTarget instanceof Transfer) {
            ((Transfer) ronTarget).receiveFrom(ronSource, 40);
        }
        System.out.println("\nAfter transfer of 40 from Marinescu to Ionescu:");
        System.out.println(bcr);

        Account eurAccount = cl1.getAccount("EU02BCRF0000000000124");
        System.out.println("\nIs EUR account transferable? " + (eurAccount instanceof Transfer));
        System.out.println("Is RON account transferable? " + (ronTarget instanceof Transfer));

        System.out.println("\n--- Balance vs Projected Total ---");
        System.out.println("Ionescu RON balance:    " + ronTarget.getBalance());
        System.out.println("Ionescu RON with interest: " + String.format("%.2f", ronTarget.getTotalAmount()));

        System.out.println("\n--- Validation demo ---");
        try {
            AccountFactory.create("not-an-iban", 100, Account.TYPE.RON);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        System.out.println("\n--- Command + Chain demo ---");
        BankOperationService operationService = new BankOperationService();

        operationService.deposit(ronTarget, 50);
        System.out.println("After command deposit(+50): " + ronTarget.getBalance());

        operationService.withdraw(ronTarget, 20);
        System.out.println("After command withdraw(-20): " + ronTarget.getBalance());

        operationService.transfer(ronTarget, ronSource, 30);
        System.out.println("After command transfer(-30/+30):");
        System.out.println("  Ionescu RON: " + ronTarget.getBalance());
        System.out.println("  Marinescu RON: " + ronSource.getBalance());

        operationService.undoLast();
        System.out.println("After undo transfer:");
        System.out.println("  Ionescu RON: " + ronTarget.getBalance());
        System.out.println("  Marinescu RON: " + ronSource.getBalance());

        operationService.redoLast();
        System.out.println("After redo transfer:");
        System.out.println("  Ionescu RON: " + ronTarget.getBalance());
        System.out.println("  Marinescu RON: " + ronSource.getBalance());

        try {
            operationService.transfer(ronTarget, ronSource, 8_000);
        } catch (IllegalStateException e) {
            System.out.println("Blocked by chain (expected): " + e.getMessage());
        }

        System.out.println("\n--- Decorator demo ---");
        Account premiumProjection = new BonusInterestDecorator(ronTarget, 0.02);
        System.out.println("Ionescu RON projected with bonus interest: "
                + String.format("%.2f", premiumProjection.getTotalAmount()));

        AuditAccountDecorator audited = new AuditAccountDecorator(ronSource);
        audited.depose(10);
        audited.retrieve(5);
        System.out.println("Audited operations on Marinescu RON: " + audited.getAuditTrail());
    }
}
