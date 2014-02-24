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
        System.out.print("\n");
        String firstPart = String.format("Scanner error (line %d): ", line);
        System.out.print(firstPart);
        // TODO print partial line
        System.out.print("\n");
        for (int i = 0; i < firstPart.length(); i++) System.out.print(" ");
        System.out.println(message);
    }
}
