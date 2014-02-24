package parser;

import scanner.Token;

public class ParserException extends Exception {
    private Token token;
    private String message;
    public ParserException(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    public void prettyPrint() {
        System.out.print("\n");
        String firstPart = String.format("Parser error (line %d):  ", token.getLine());
        System.out.print(firstPart);
        // TODO print partial line
        System.out.print("\n");
        for (int i = 0; i < firstPart.length(); i++) System.out.print(" ");
        System.out.println(message);
    }
}
