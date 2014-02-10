package scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
        List<ScannerState> tokens = new ArrayList<ScannerState>();
        TokenResolver resolver = new TokenResolver();

        ScannerState oldState = resolver.getState();
        ScannerState newState = null;

        try {
            char c = (char) br.read();
            while (c != (char) -1) {
                oldState = resolver.getState();
                newState = resolver.tokenize(c);
                if (newState == null) {
                    if (oldState.isTerminalState()) {
                        tokens.add(oldState);
                    }
                    else {
                        tokens.add(ScannerState.ID);
                    }
                    resolver.reset();
                    continue;
                }
                c = (char) br.read();
            }
            tokens.add(newState);
        } finally {
            br.close();
        }

        for (ScannerState state : tokens) {
            System.err.println(state.name());
        }
    }
}
