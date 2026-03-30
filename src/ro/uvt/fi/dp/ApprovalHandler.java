package ro.uvt.fi.dp;

public class ApprovalHandler extends TransactionHandler {
    @Override
    protected void validate(TransactionRequest request) {
        // Terminal handler: if execution reached here, request is approved.
    }
}
