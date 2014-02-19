package scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 2/9/14.
 */
public class Scanner {
    //run this method to test the scanner
    public static void main (String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("test.txt"));
        List<StateName> tokens = new ArrayList<StateName>();
        TokenResolver resolver = new TokenResolver();

        State oldState = resolver.getState();
        State newState = null;
        boolean parsingComment = false;

        try {
            char c = (char) br.read();
            while (c != (char) -1) {
                if (parsingComment) {
                    String scan = "";
                    while (!scan.endsWith("*/")) {
                        scan+=String.valueOf((char) br.read());
                    }
                    c = (char) br.read();
                    parsingComment = false;
                }
                oldState = resolver.getState();
                newState = resolver.tokenize(c);
                if (newState == null) {
                    if (tokens.size() > 1 && tokens.get(tokens.size()-1) == StateName.DIV && oldState.getStateName() == StateName.MULT) {
                        parsingComment = true;
                        tokens.remove(tokens.size()-1);
                    }
                    if (oldState.getStateName().isTerminalState()) {
                        if (!parsingComment) tokens.add(oldState.getStateName());
                    }
                    else {
                        if (!parsingComment) tokens.add(StateName.ID);
                    }
                    resolver.reset();
                    continue;
                }
                c = (char) br.read();
            }
            tokens.add(newState.getStateName());
        } finally {
            br.close();
        }

        //TODO this is probably not the right way to handle this, but the code currently appends a CHARACTER_ACCEPT state to every file parsed.  So we're removing it.
        tokens.remove(tokens.size()-1);

        for (StateName state : tokens) {
            System.err.println(state.name());
        }
    }
}
