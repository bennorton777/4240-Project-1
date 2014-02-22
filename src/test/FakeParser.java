package test;

import scanner.Scanner;
import scanner.State;
import scanner.Token;

import java.io.IOException;

/**
 * This class exists to demonstrate how to call the Scanner incrementally.
 * It also serves to test the Scanner independently of other components.
 */
public class FakeParser {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner("test.txt");
        Token token = scanner.getNextToken();
        while (token != null) {
            System.out.println(token);
            token = scanner.getNextToken();
        }
    }
}
