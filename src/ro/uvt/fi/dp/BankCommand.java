package ro.uvt.fi.dp;

public interface BankCommand {
    void execute();

    void undo();

    String description();
}
