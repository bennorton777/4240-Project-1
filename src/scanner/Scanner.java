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

        try {
            char c = (char) br.read();
            while (c != (char) -1) {
                oldState = resolver.getState();
                newState = resolver.tokenize(c);
                if (newState == null) {
                    if (oldState.getStateName().isTerminalState()) {
                        tokens.add(oldState.getStateName());
                    }
                    else {
                        tokens.add(StateName.ID);
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

        for (StateName state : tokens) {
            System.err.println(state.name());
        }
    }
}
