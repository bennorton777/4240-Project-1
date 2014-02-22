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

		try {
            Scanner scanner = new Scanner(args[0]);
			List<Token> tokens = scanner.getTokens();

			tokens.add(new Token(RuleResolver.EOF_SYMBOL,
					RuleResolver.EOF_SYMBOL));

			// for (Token tok : tokens) {
			// System.err.println(tok.getType());
			// }

			RuleResolver parserTable = new RuleResolver();
			symbolsToMatch.push(RuleResolver.EOF_SYMBOL);
			symbolsToMatch.push(RuleResolver.START_SYMBOL);

			parserTable.printTable();
			
			String input, top;
			for (Token tok : tokens) {
				input = tok.getType();
				top = symbolsToMatch.pop();
				while (!top.equals(input)
						&& !top.equals(RuleResolver.NULL_SYMBOL)) {
					if (!parserTable.isNonTerminal(top))
						throw (new Exception("NoWhereToGo : Terminal symbol '"
								+ top + "' does not match input symbol '"
								+ input + "'"));
					Rule expansion = parserTable.getRule(top, input);
					// System.out.println("Expanding rule:" + expansion);
					if (expansion == null)
						throw (new Exception("No expansion for symbol '" + top
								+ "' on input symbol '" + input + "'"));
					String rhs[] = expansion.getSeq();
					for (int i = rhs.length - 1; i >= 0; i--) {
						symbolsToMatch.push(rhs[i]);
					}

					System.out.println();
					top = symbolsToMatch.pop();
					// System.out.println("Loop matching symbol '" + top
					// + "' for input symbol '" + input + "'");
				}
			}
			System.out.println("Parse successfull");

		} catch (GrammarException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Invalid filename..");
		} catch (IOException e) {
			System.out.println("IOException while reading file..");
		} catch (Exception e) {
			System.out.println(e);
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
