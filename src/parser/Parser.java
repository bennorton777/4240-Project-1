package parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import parser.RuleResolver.GrammarException;

import scanner.Scanner;
import scanner.Token;

public class Parser {
	public static void main(String[] args) {

		Stack<String> symbolsToMatch = new Stack<String>();

		if (args.length < 1) {
			System.out.println("Provide a tiger program to parse..");
			return;
		}

		boolean show_flow = false;

		try {
			if (show_flow)
				Scanner.printTokens(args[0]);
			Scanner scanner = new Scanner(args[0]);
			RuleResolver parserTable = new RuleResolver();

			// List<Token> tokens = scanner.getTokens();
			// tokens.add(new Token(RuleResolver.EOF_SYMBOL,
			// RuleResolver.EOF_SYMBOL));

			// for (Token tok : tokens) {
			// System.err.print(tok.getType() + " ");
			// }

			symbolsToMatch.push(RuleResolver.EOF_SYMBOL);
			symbolsToMatch.push(parserTable.START_SYMBOL);

			// parserTable.printTable();
			// System.out.println();

			String input, top;
			Token tok = scanner.getNextToken();
			while (!tok.getType().equals(RuleResolver.EOF_SYMBOL)) {
				input = tok.getType();
				top = symbolsToMatch.pop();
				if (show_flow)
					System.out.println("Matching symbol '" + top
							+ "' for input symbol '" + input + "'");
				while (!top.equals(input)) {
					if (!parserTable.isNonTerminal(top))
						throw (new Exception("NoWhereToGo : Terminal symbol '"
								+ top + "' does not match input symbol '"
								+ input + "'"));
					Rule expansion = parserTable.getRule(top, input);
					if (show_flow)
						System.out.println("Expanding rule:" + expansion);
					if (expansion == null)
						throw (new Exception("No expansion for symbol '" + top
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

		} catch (GrammarException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Invalid filename..");
		} catch (IOException e) {
			System.out.println("IOException while reading file..");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printStack(Stack<String> symbolsToMatch) {
		System.out.print("Stack: ");
		for (String sym : symbolsToMatch) {
			System.out.print(sym + " ");
		}
	}

}
