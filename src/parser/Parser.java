package parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

import parser.RuleResolver.GrammarException;

import scanner.Scanner;
import scanner.ScannerException;
import scanner.Token;

public class Parser {
	public static void main(String[] args) {

		Stack<String> symbolsToMatch = new Stack<String>();

		if (args.length < 1) {
			System.out.println("Provide a tiger program to parse..");
			return;
		}

		boolean show_flow = false;

        Scanner scanner;
        RuleResolver parserTable;
		try {
			scanner = new Scanner(args[0]);
			parserTable = new RuleResolver();
        } catch (GrammarException e) {
			e.printStackTrace();
            return;
        } catch (IOException e) {
			System.out.println("IOException while reading file..");
            return;
		}
        try {
            symbolsToMatch.push(RuleResolver.EOF_SYMBOL);
            symbolsToMatch.push(parserTable.START_SYMBOL);

            String input, top;
            try {
                Token tok = scanner.getNextToken();
                while (!tok.getType().equals(RuleResolver.EOF_SYMBOL)) {
                    input = tok.getType();
                    top = symbolsToMatch.pop();
                    if (show_flow)
                        System.out.println("Matching symbol '" + top
                                + "' for input symbol '" + input + "'");
                    while (!top.equals(input)) {
                        if (!parserTable.isNonTerminal(top))
                            throw (new ParserException(tok, "NoWhereToGo : Terminal symbol '"
                                    + top + "' does not match input symbol '"
                                    + input + "'"));
                        Rule expansion = parserTable.getRule(top, input);
                        if (show_flow)
                            System.out.println("Expanding rule:" + expansion);
                        if (expansion == null)
                            throw (new ParserException(tok, "No expansion for symbol '" + top
                                    + "' on input symbol '" + input + "'"));
                        String rhs[] = expansion.getSeq();
                        for (int i = rhs.length - 1; i >= 0; i--) {
                            symbolsToMatch.push(rhs[i]);
                        }

                        do {
                            top = symbolsToMatch.pop();
                        } while (top.equals(RuleResolver.NULL_SYMBOL));
                        if (show_flow)
                            System.out.println("Loop matching symbol '" + top
                                    + "' for input symbol '" + input + "'");
                    }
                    tok = scanner.getNextToken();
                    if (show_flow)
                        System.out.println();
                }
                System.out.println("successful parse");

            } catch (ScannerException e) {
                e.prettyPrint();
                // print the rest of the tokens anyway
                Token tok = null;
                scanner.afterError();
                do {
                    try {
                        tok = scanner.getNextToken();
                    } catch (ScannerException ex) {
                        ex.prettyPrint();
                        scanner.afterError();
                        tok = new Token("DUMMY", "DUMMY", 0, 0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        break;
                    }
                } while(tok != null && !tok.getType().equals(RuleResolver.EOF_SYMBOL));
                System.out.println("unsuccessful parse");
            } catch (ParserException e) {
                e.prettyPrint();
                System.out.println("unsuccessful parse");
            }
		} catch (FileNotFoundException e) {
			System.out.println("Invalid filename..");
		} catch (IOException e) {
			System.out.println("IOException while reading file..");
		}
	}

	public static void printStack(Stack<String> symbolsToMatch) {
		System.out.print("Stack: ");
		for (String sym : symbolsToMatch) {
			System.out.print(sym + " ");
		}
	}

}
