package ro.uvt.fi.dp;

import java.time.LocalDate;

public class AccountTest {

    private static void assertTrue(String testName, boolean condition) {
        if (!condition) {
            throw new AssertionError("FAIL: " + testName);
        }
        System.out.println("PASS: " + testName);
    }

    private static void assertEquals(String testName, double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError("FAIL: " + testName
                    + " — expected " + expected + " but got " + actual);
        }
        System.out.println("PASS: " + testName);
    }

    static void testFactoryCreatesRonAccount() {
        Account a = Account.of("RO49AAAA0000000000001", 300, Account.TYPE.RON);
        assertTrue("factory creates RonAccount", a instanceof RonAccount);
    }

    static void testFactoryCreatesEurAccount() {
        Account a = Account.of("EU02AAAA0000000000001", 300, Account.TYPE.EUR);
        assertTrue("factory creates EurAccount", a instanceof EurAccount);
    }

    static void testValidIbanAccepted() {
        Account a = Account.of("RO49AAAA0000000000001", 100, Account.TYPE.RON);
        assertTrue("valid IBAN accepted", a != null);
    }

    static void testInvalidIbanRejected() {
        boolean threw = false;
        try {
            Account.of("not-an-iban!!", 100, Account.TYPE.RON);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assertTrue("invalid IBAN rejected with IllegalArgumentException", threw);
    }

    static void testNullIbanRejected() {
        boolean threw = false;
        try {
            Account.of(null, 100, Account.TYPE.RON);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assertTrue("null IBAN rejected with IllegalArgumentException", threw);
    }

    static void testRonLowInterestBelowThreshold() {
        Account a = Account.of("RO49AAAA0000000000002", 300, Account.TYPE.RON);
        assertEquals("RON interest below threshold is 0.03", 0.03, a.getInterest(), 0.0001);
    }

    static void testRonHighInterestAboveThreshold() {
        Account a = Account.of("RO49AAAA0000000000003", 600, Account.TYPE.RON);
        assertEquals("RON interest above threshold is 0.08", 0.08, a.getInterest(), 0.0001);
    }

    static void testEurFlatInterest() {
        Account a = Account.of("EU02AAAA0000000000002", 1000, Account.TYPE.EUR);
        assertEquals("EUR interest is always 0.01", 0.01, a.getInterest(), 0.0001);
    }

    static void testGetBalanceReturnsRawAmount() {
        Account a = Account.of("RO49AAAA0000000000004", 200, Account.TYPE.RON);
        assertEquals("getBalance() returns raw amount", 200.0, a.getBalance(), 0.0001);
    }

    static void testGetTotalAmountIncludesInterest() {
        Account a = Account.of("RO49AAAA0000000000005", 200, Account.TYPE.RON);
        assertEquals("getTotalAmount() = balance + interest projection", 206.0, a.getTotalAmount(), 0.0001);
    }

    static void testGetBalanceAndTotalAreDifferent() {
        Account a = Account.of("RO49AAAA0000000000006", 200, Account.TYPE.RON);
        assertTrue("getBalance() and getTotalAmount() are not equal", a.getBalance() != a.getTotalAmount());
    }

    static void testReceiveFromDebitsSource() {
        RonAccount source = (RonAccount) Account.of("RO49AAAA0000000000007", 500, Account.TYPE.RON);
        RonAccount dest   = (RonAccount) Account.of("RO49AAAA0000000000008", 100, Account.TYPE.RON);

        dest.receiveFrom(source, 200);

        assertEquals("source debited after receiveFrom", 300.0, source.getBalance(), 0.0001);
        assertEquals("destination credited after receiveFrom", 300.0, dest.getBalance(), 0.0001);
    }

    static void testTransferDelegateCorrectly() {
        RonAccount source = (RonAccount) Account.of("RO49AAAA0000000000009", 500, Account.TYPE.RON);
        RonAccount dest   = (RonAccount) Account.of("RO49AAAA0000000000010", 100, Account.TYPE.RON);

        dest.transfer(source, 100);

        assertEquals("transfer() debits source correctly", 400.0, source.getBalance(), 0.0001);
        assertEquals("transfer() credits dest correctly", 200.0, dest.getBalance(), 0.0001);
    }

    static void testEurAccountNotTransferable() {
        Account eur = Account.of("EU02AAAA0000000000003", 500, Account.TYPE.EUR);
        assertTrue("EurAccount does not implement Transfer", !(eur instanceof Transfer));
    }

    static void testRonAccountIsTransferable() {
        Account ron = Account.of("RO49AAAA0000000000011", 500, Account.TYPE.RON);
        assertTrue("RonAccount implements Transfer", ron instanceof Transfer);
    }

    static void testFormatterContainsCurrencyLabel() {
        Account ron = Account.of("RO49AAAA0000000000012", 100, Account.TYPE.RON);
        String formatted = AccountFormatter.format(ron);
        assertTrue("formatter includes 'RON'", formatted.contains("RON"));
    }

    static void testFormatterContainsAccountCode() {
        Account eur = Account.of("EU02AAAA0000000000004", 100, Account.TYPE.EUR);
        String formatted = AccountFormatter.format(eur);
        assertTrue("formatter includes the account code", formatted.contains("EU02AAAA0000000000004"));
    }

    static void testDepose() {
        Account a = Account.of("RO49AAAA0000000000013", 100, Account.TYPE.RON);
        a.depose(50);
        assertEquals("depose increases balance", 150.0, a.getBalance(), 0.0001);
    }

    static void testRetrieve() {
        Account a = Account.of("RO49AAAA0000000000014", 200, Account.TYPE.RON);
        a.retrieve(80);
        assertEquals("retrieve decreases balance", 120.0, a.getBalance(), 0.0001);
    }

    static void testClientBuilderCreatesOptionalFields() {
        Client c = Client.builder("Popescu Ana")
                .address("Cluj")
                .birthDay(LocalDate.of(1998, 5, 20))
                .premium(true)
                .initialAccount(Account.TYPE.EUR, "EU02AAAA0000000000500", 500)
                .build();

        assertTrue("builder creates client account", c.getAccount("EU02AAAA0000000000500") != null);
    }

    static void testFactoryCreatesByType() {
        Account eur = AccountFactory.create("EU02AAAA0000000000777", 77, Account.TYPE.EUR);
        Account ron = AccountFactory.create("RO49AAAA0000000000777", 77, Account.TYPE.RON);
        assertTrue("factory creates eur", eur instanceof EurAccount);
        assertTrue("factory creates ron", ron instanceof RonAccount);
    }

    static void testSingletonReturnsSameInstance() {
        BankRegistry r1 = BankRegistry.getInstance();
        BankRegistry r2 = BankRegistry.getInstance();
        assertTrue("singleton returns same instance", r1 == r2);
    }

    public static void main(String[] args) {
        System.out.println("=== Running AccountTest ===\n");
        testFactoryCreatesRonAccount();
        testFactoryCreatesEurAccount();
        testValidIbanAccepted();
        testInvalidIbanRejected();
        testNullIbanRejected();
        testRonLowInterestBelowThreshold();
        testRonHighInterestAboveThreshold();
        testEurFlatInterest();
        testGetBalanceReturnsRawAmount();
        testGetTotalAmountIncludesInterest();
        testGetBalanceAndTotalAreDifferent();
        testReceiveFromDebitsSource();
        testTransferDelegateCorrectly();
        testEurAccountNotTransferable();
        testRonAccountIsTransferable();
        testFormatterContainsCurrencyLabel();
        testFormatterContainsAccountCode();
        testDepose();
        testRetrieve();
        testClientBuilderCreatesOptionalFields();
        testFactoryCreatesByType();
        testSingletonReturnsSameInstance();

        System.out.println("\n=== All tests passed! ===");
    }
}
