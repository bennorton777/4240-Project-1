package scanner;

import com.sun.deploy.util.ArrayUtil;

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
        List<State> tokens = new ArrayList<State>();
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
                        c = (char) br.read();
                        scan+=String.valueOf(c);
                    }
                    c = (char) br.read();
                    parsingComment = false;
                }
                oldState = resolver.getState();
                newState = resolver.tokenize(c);
                if (newState == null) {
                    if (tokens.size() > 1 && tokens.get(tokens.size()-1).getStateName() == StateName.DIV && oldState.getStateName() == StateName.MULT) {
                        parsingComment = true;
                        tokens.remove(tokens.size()-1);
                    }
                    if (!parsingComment) {
                        if (oldState.getStateName().isTerminalState()) {
                            tokens.add(new State(oldState));
                        }
                        else {
                            State idState = new State(StateName.ID);
                            idState.setDisplayText(oldState.getDisplayText());
                            tokens.add(idState);
                        }
                    }
                    resolver.reset();
                    continue;
                }
                c = (char) br.read();
            }
        } finally {
            br.close();
        }

        StringBuilder sb = new StringBuilder();
        for (State state : tokens) {
            System.err.println(state);
            sb.append(state.getDisplayText());
            sb.append(" ");
        }
        System.err.println("\n"+sb);
    }
}
