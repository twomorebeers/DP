package ro.uvt.fi.dp;

public class Test {

    public static void main(String[] args) {

        // --- BCR bank: two clients ---
        Bank bcr = new Bank("BCR Bank");

        // Ionescu has both an EUR and a RON account
        // Note: account codes now must follow a basic IBAN pattern (Issue 7)
        Client cl1 = new Client("Ionescu Ion", "Timisoara", Account.TYPE.EUR, "EU02BCRF0000000000124", 200.9);
        bcr.addClient(cl1);
        cl1.addAccount(Account.TYPE.RON, "RO49BCRF0000001234567", 400);

        // Marinescu has a single RON account
        Client cl2 = new Client("Marinescu Marin", "Timisoara", Account.TYPE.RON, "RO49BCRF0000001260000", 100);
        bcr.addClient(cl2);

        System.out.println("=== Initial state ===");
        System.out.println(bcr);

        // --- CEC bank: one client ---
        Bank cec = new Bank("CEC Bank");
        Client clientCEC = new Client("Vasilescu Vasile", "Brasov", Account.TYPE.EUR, "EU02CECF0000000000128", 700);
        cec.addClient(clientCEC);
        System.out.println(cec);

        // --- Operations on Marinescu's account ---
        Client cl = bcr.getClient("Marinescu Marin");
        if (cl != null) {
            cl.getAccount("RO49BCRF0000001260000").depose(400);
            System.out.println("\nAfter deposit of 400:");
            System.out.println(cl);

            cl.getAccount("RO49BCRF0000001260000").retrieve(67);
            System.out.println("\nAfter retrieval of 67:");
            System.out.println(cl);
        }

        // --- Transfer: Issue 5 fix demo ---
        // receiveFrom() makes it obvious that ronTarget receives money FROM ronSource
        Account ronSource = cl.getAccount("RO49BCRF0000001260000");
        Account ronTarget = bcr.getClient("Ionescu Ion").getAccount("RO49BCRF0000001234567");

        // ronTarget (Ionescu's RON account) receives 40 from ronSource (Marinescu's RON account)
        if (ronTarget instanceof Transfer) {
            ((Transfer) ronTarget).receiveFrom(ronSource, 40);
        }
        System.out.println("\nAfter transfer of 40 from Marinescu to Ionescu:");
        System.out.println(bcr);

        // --- ISP demo: EUR account does NOT have transfer capability (Issue 6) ---
        Account eurAccount = cl1.getAccount("EU02BCRF0000000000124");
        System.out.println("\nIs EUR account transferable? " + (eurAccount instanceof Transfer));
        System.out.println("Is RON account transferable? " + (ronTarget instanceof Transfer));

        // --- getBalance() vs getTotalAmount() demo (Issue 4) ---
        System.out.println("\n--- Balance vs Projected Total ---");
        System.out.println("Ionescu RON balance:    " + ronTarget.getBalance());
        System.out.println("Ionescu RON with interest: " + String.format("%.2f", ronTarget.getTotalAmount()));

        // --- Invalid IBAN demo (Issue 7) ---
        System.out.println("\n--- Validation demo ---");
        try {
            Account bad = Account.of("not-an-iban", 100, Account.TYPE.RON);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
    }
}
