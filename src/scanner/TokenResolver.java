package scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by ben on 2/9/14.
 */
public class TokenResolver {

    private Map<ScannerState, ResolutionStrategy> strategies;
    private ScannerState state;

    public TokenResolver() throws IOException {

        strategies = new HashMap<ScannerState, ResolutionStrategy>();
        BufferedReader br = new BufferedReader(new FileReader("4240 Phase 1 DFA.csv"));

        try {
            String line = br.readLine();

            while (line != null) {
                //For every line, elements should have the initial state at index zero, the acceptable character class
                // for that line's strategy at index 1, and the transition state at index 2.
                String[] elements = line.split(",");

                //TODO This next line could fail if the table is malformed.  There should probably be a check in place
                // so that an exception could be thrown if applicable.
                ScannerState initialState = Enum.valueOf(ScannerState.class, elements[0].trim().toUpperCase());
                CharacterClass accepts = Enum.valueOf(CharacterClass.class, elements[1].trim().toUpperCase());
                ScannerState finalState = Enum.valueOf(ScannerState.class, elements[2].trim().toUpperCase());
                if (strategies.get(initialState) == null) {
                    strategies.put(initialState, new ResolutionStrategy());
                }

                strategies.get(initialState).addStrategy(accepts, finalState);

                line = br.readLine();
            }

            state = ScannerState.CHARACTER_ACCEPT;
        } finally {
            br.close();
        }
    }

    public ScannerState tokenize(Character c) {
        ResolutionStrategy strategy = strategies.get(state);

        // If there is no resolution strategy, then we must be at a terminal state.
        if (strategy == null) {
            return null;
        }

        // We're going to mutate this set to help us select which character class to investigate.
        // Copying the set prevents us from accidentally mutating the underlying strategy by accident.
        Set<CharacterClass> acceptableClasses = new HashSet<CharacterClass>(strategy.getAcceptableCharacterClasses());

        while (acceptableClasses.size() > 0) {
            CharacterClass mostSpecificClass = null;
            double priority = Float.POSITIVE_INFINITY;
            for (CharacterClass characterClass : acceptableClasses) {
                if (characterClass.getPriority() < priority) {
                    priority = characterClass.getPriority();
                    mostSpecificClass = characterClass;
                }
            }
            acceptableClasses.remove(mostSpecificClass);
            if (mostSpecificClass.resolve(c)) {
                state = strategy.nextState(mostSpecificClass);
                return state;
            }
        }
        // Return null unless we have successfully changed state
        return null;
    }

    public ScannerState getState() {
        return state;
    }

    public void reset() {
        state = ScannerState.CHARACTER_ACCEPT;
    }

}
