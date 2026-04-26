package ro.uvt.fi.dp;

import java.util.ArrayDeque;
import java.util.Deque;

public class BankCommandManager {
    private final Deque<BankCommand> undoStack = new ArrayDeque<>();
    private final Deque<BankCommand> redoStack = new ArrayDeque<>();

    public void execute(BankCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undoLast() {
        if (!canUndo()) {
            return;
        }
        BankCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redoLast() {
        if (!canRedo()) {
            return;
        }
        BankCommand command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
}
