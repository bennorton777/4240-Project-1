package scanner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import parser.RuleResolver;

/**
 * The scanner is responsible for taking a filename corresponding to the input
 * program, and using the {@link scanner.TokenResolver} to compose a list of
 * {@link scanner.State} objects. These {@link scanner.State} objects are the
 * tokens referred to in the assignment writeup.
 */
public class Scanner {
	/**
	 * This method reads through a program text, and uses the
	 * {@link scanner.TokenResolver} to create a list of tokens. TODO
	 * Application's main method should not be in Scanner.
	 * 
	 * @param args
	 * @throws IOException
	 */

	private char _currentChar;
	BufferedReader _br;
	TokenResolver _resolver;
	boolean done = false;

	// The oldState refers to the state of the TokenResolver before reading in
	// the most recent character,
	// whereas newState refers to the state afterwards.
	State oldState, newState = null;

	public Scanner(String inputFileName) throws FileNotFoundException,
			IOException {

		_resolver = new TokenResolver();
		_br = new BufferedReader(new FileReader(inputFileName));
		// This function changes the file the buffered reader points at.
		cleanComments();

		_currentChar = (char) _br.read();
	}

	public Token getNextToken() throws IOException {
		if (done)
			return new Token(RuleResolver.EOF_SYMBOL, RuleResolver.EOF_SYMBOL);

		// We want the TokenResolver to reset its state after we add a token.
		_resolver.reset();

		// While the character assigned to _currentChar isn't the EOF character
		while (_currentChar != (char) -1) {
			oldState = _resolver.getState();
			// The tokenize method returns the state of the TokenResolver, so we
			// don't need a separate call.
			newState = _resolver.tokenize(_currentChar);

			// The state of the TokenResolver will only be null if the character
			// most recently received does not
			// have a valid transition defined from the state the TokenResolver
			// before receiving that character.
			// If this is the case, then the state prior to receiving the most
			// recent character must refer to a
			// token.
			// TODO This assumes that the input program will never be
			// syntactically incorrect. This will need to change.
			if (newState == null) {
				if (oldState.getStateName().isTerminalState()) {
					// Note that we're making a new state. That's very
					// important, because states are mutated
					// as the application runs, and we don't want our list of
					// tokens to be corrupted.
					return new Token(oldState.getStateName().name(),
							oldState.getDisplayText());
				}
				// The CHARACTER_ACCEPT state is not a terminal state, whereas
				// ID is. However, it's not always
				// clear that we are dealing with an ID until the newState
				// becomes null.
				else {
					Token temp = new Token(StateName.ID.name(), oldState.getDisplayText());
                    // If the token value is empty, it means our scanner did not find a state for collected input.
                    // So, we throw an exception. Otherwise, we can just return the token.
                    if (temp.getValue() == "")
                        throw new IOException("There is a syntax error in your code. Please correct this.");
                    return temp;
				}
			}
			_currentChar = (char) _br.read();
		}
		_br.close();
		done = true;
		if (newState.getStateName().name().equals("CHARACTER_ACCEPT"))
			return new Token(RuleResolver.EOF_SYMBOL, RuleResolver.EOF_SYMBOL);
		return new Token(newState.getStateName().name(),
				newState.getDisplayText());
	}

	// DEPRECATED
	public List<Token> getTokens() throws IOException {
		List<Token> tokens = new ArrayList<Token>();
		Token token = getNextToken();
		while (!token.getType().equals(RuleResolver.EOF_SYMBOL)) {
			tokens.add(token);
			token = getNextToken();
		}
		return tokens;
	}

	// TODO this will throw an IOException (or some exception, anyway) if given
	// an input program with fewer than two characters in it. That's pretty
	// dumb.
	private void cleanComments() throws IOException {
		boolean parsingComment = false;
		PrintWriter writer = new PrintWriter("clean.txt", "UTF-8");
		ArrayList<String> symbols = new ArrayList<String>();
		String c = String.valueOf((char) _br.read());
		symbols.add(c);
		while (!c.equals(String.valueOf((char) -1))) {
			if (parsingComment) {
				String scan = "";
				while (!scan.endsWith("*/")) {
					c = String.valueOf((char) _br.read());
					scan += c;
				}
				c = String.valueOf((char) _br.read());
				parsingComment = false;
			}
			c = String.valueOf((char) _br.read());
			if (c.equals("*") && symbols.get(symbols.size() - 1).equals("/")) {
				symbols.remove(symbols.size() - 1);
				parsingComment = true;
				continue;
			}
			if (!c.equals(String.valueOf((char) -1)))
				symbols.add(c);
		}
		for (String s : symbols) {
			writer.print(s);
		}
		writer.close();
		_br.close();
		_br = new BufferedReader(new FileReader("clean.txt"));
	}

	public static void printTokens(String fname) throws FileNotFoundException,
			IOException {
		Scanner scanner = new Scanner(fname);
		Token token = scanner.getNextToken();
		while (!token.getType().equals(RuleResolver.EOF_SYMBOL)) {
			System.out.print(token.getType());
			token = scanner.getNextToken();
			if (!token.getType().equals(RuleResolver.EOF_SYMBOL)) System.out.print(" ");						
		}
		System.out.print("\n");
	}

}
