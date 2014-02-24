package scanner;

import java.io.IOException;

public class ScannerException extends Exception {
    private String filename;
    private int line;
    private int column;
    private String message;
    public ScannerException(String filename, int line, int column, String message) {
        this.filename = filename;
        this.line = line;
        this.column = column;
        this.message = message;
    }
    public void prettyPrint() {
        System.out.print("\n");
        String firstPart = String.format("Scanner error (line %d): ", line);
        System.out.print(firstPart);
        try {
            System.out.print(ScannerUtil.getPartString(filename, line, column));
            System.out.print("<---");
        } catch (IOException e) {}
        System.out.print("\n");
        for (int i = 0; i < firstPart.length(); i++) System.out.print(" ");
        System.out.println(message);
    }
}
