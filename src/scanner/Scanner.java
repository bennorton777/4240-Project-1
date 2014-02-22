package scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The scanner is responsible for taking a filename corresponding to the input program,
 * and using the {@link scanner.TokenResolver} to compose a list of {@link scanner.State} objects.
 * These {@link scanner.State} objects are the tokens referred to in the assignment writeup.
 */
public class Scanner {
    /**
     * This method reads through a program text, and uses the {@link scanner.TokenResolver} to
     * create a list of tokens.
     * TODO Application's main method should not be in Scanner.
     * @param args
     * @throws IOException
     */
    public static void main (String[] args) throws IOException {

        List<Token> tokens = getTokens("test.txt");
        // TODO These print statements are debugging statements.  They should not be left in the final application.
        for (Token tok : tokens) {
            System.err.println(tok);           
        }
    }

	public static List<Token> getTokens(String inputFileName) throws FileNotFoundException,
			IOException {
		BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        List<State> tokenStates = new ArrayList<State>();
        TokenResolver resolver = new TokenResolver();

        // The oldState refers to the state of the TokenResolver before reading in the most recent character,
        // whereas newState refers to the state afterwards.
        State oldState, newState = null;

        // We don't want to be trying to resolve tokens inside of a comment.
        boolean parsingComment = false;

        try {
            char c = (char) br.read();
            // While the character assigned to c isn't the EOF character
            while (c != (char) -1) {

                if (parsingComment) {
                    String scan = "";
                    while (!scan.endsWith("*/")) {
                        c = (char) br.read();
                        scan+=String.valueOf(c);
                    }
                    c = (char) br.read();
                    parsingComment = false;
                }

                oldState = resolver.getState();
                // The tokenize method returns the state of the TokenResolver, so we don't need a separate call.
                newState = resolver.tokenize(c);

                // The state of the TokenResolver will only be null if the character most recently received does not
                // have a valid transition defined from the state the TokenResolver before receiving that character.
                // If this is the case, then the state prior to receiving the most recent character must refer to a
                // token.
                // TODO This assumes that the input program will never be syntactically incorrect.  This will need to change.
                if (newState == null) {
                    // Here we check to see if we got /* back to back, which tells us we're starting a comment.
                    if (tokenStates.size() > 1 && tokenStates.get(tokenStates.size()-1).getStateName() == StateName.DIV && oldState.getStateName() == StateName.MULT) {
                        parsingComment = true;
                        // We don't want the /* to be a part of the scanned input.
                        tokenStates.remove(tokenStates.size()-1);
                    }
                    // We don't want to add to our list of tokens if we're currently dealing with a comment.
                    if (!parsingComment) {
                        if (oldState.getStateName().isTerminalState()) {
                            // Note that we're making a new state.  That's very important, because states are mutated
                            // as the application runs, and we don't want our list of tokens to be corrupted.
                            tokenStates.add(new State(oldState));
                        }
                        // The CHARACTER_ACCEPT state is not a terminal state, whereas ID is.  However, it's not always
                        // clear that we are dealing with an ID until the newState becomes null.
                        else {
                            State idState = new State(StateName.ID);
                            idState.setDisplayText(oldState.getDisplayText());
                            tokenStates.add(idState);
                        }
                    }
                    // We want the TokenResolver to reset its state after we add a token.
                    resolver.reset();
                    // We don't want to read a new character when we add a token to the list, because the last character
                    // considered will always be the character that begins the next token.  So we skip the read command below.
                    continue;
                }
                c = (char) br.read();
            }
        } finally {
            br.close();
        }
        
        List<Token> tokens = new ArrayList<Token>();
        
        for (State state : tokenStates) {
			tokens.add(new Token(state.getStateName().name(), state.getDisplayText()));
		}
        
		return tokens;
	}
}
