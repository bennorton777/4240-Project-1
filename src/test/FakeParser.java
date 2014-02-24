package test;

import scanner.Scanner;
import scanner.ScannerException;
import scanner.State;
import scanner.Token;

import java.io.IOException;
import java.util.List;

import parser.RuleResolver;

/**
 * This class exists to demonstrate how to call the Scanner incrementally.
 * It also serves to test the Scanner independently of other components.
 */
public class FakeParser {
    public static void main(String[] args) throws IOException {
    	
        System.out.println("Incremental:");
        // do it incrementally
        Scanner scanner = new Scanner("test.txt");
        Token token = null;
        try {
            token = scanner.getNextToken();
        } catch (ScannerException e) {
            e.prettyPrint();
        }
        while (token != null && !token.getType().equals(RuleResolver.EOF_SYMBOL)) {
            System.out.println(token);
            try {
                token = scanner.getNextToken();
            } catch (ScannerException e) {
                e.prettyPrint();
                break;
            }
        }

        System.out.println("\nAll at once:");
        // Do it all at once
        scanner = new Scanner("test.txt");
        try {
            List<Token> tokens = scanner.getTokens();
            for (Token t : tokens) {
                System.out.println(t);
            }
        } catch (ScannerException e) {
            e.prettyPrint();
        }
    }
}
