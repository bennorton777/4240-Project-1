package scanner;

public class ScannerException extends Exception {
    private int line;
    private int column;
    private String message;
    public ScannerException(int line, int column, String message) {
        this.line = line;
        this.column = column;
        this.message = message;
    }
    public void prettyPrint() {
        // TODO
        this.printStackTrace();
    }
}
