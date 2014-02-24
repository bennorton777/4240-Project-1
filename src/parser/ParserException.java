package parser;

import scanner.ScannerUtil;
import scanner.Token;

import java.io.IOException;

public class ParserException extends Exception {
    private String filename;
    private Token token;
    private String message;
    public ParserException(String filename, Token token, String message) {
        this.filename = filename;
        this.token = token;
        this.message = message;
    }

    public void prettyPrint() {
        System.out.print("\n");
        String firstPart = String.format("Parser error (line %d):  ", token.getLine());
        System.out.print(firstPart);
        try {
            System.out.print(ScannerUtil.getPartString(filename, token.getLine(), token.getColumn()));
            System.out.print("<---");
        } catch (IOException e) {}
        System.out.print("\n");
        for (int i = 0; i < firstPart.length(); i++) System.out.print(" ");
        System.out.println(message);
    }
}
