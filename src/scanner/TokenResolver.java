package scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by ben on 2/9/14.
 */
public class TokenResolver {

    private Map<State, ResolutionStrategy> strategies;
    private State state;

    public TokenResolver() throws IOException {

        strategies = new HashMap<State, ResolutionStrategy>();
        BufferedReader br = new BufferedReader(new FileReader("4240 Phase 1 DFA.csv"));

        try {
            String line = br.readLine();

            while (line != null) {
                //For every line, elements should have the initial state at index zero, the acceptable character class
                // for that line's strategy at index 1, and the transition state at index 2.
                String[] elements = line.split("\t");

                String initialStatePrefix = "";
                StateName initialStateName = null;
                String finalStatePrefix = "";
                StateName finalStateName = null;
                CharacterClass acceptClass = null;
                String acceptCharacter = "";


                try {
                    initialStateName = Enum.valueOf(StateName.class, elements[0].trim().toUpperCase());
                } catch(IllegalArgumentException e) {
                    initialStatePrefix = elements[0].trim();
                } try {
                    finalStateName = Enum.valueOf(StateName.class, elements[2].trim().toUpperCase());
                } catch(IllegalArgumentException e) {
                    finalStatePrefix = elements[2].trim();
                } try {
                    acceptClass = Enum.valueOf(CharacterClass.class, elements[1].trim().toUpperCase());
                } catch(IllegalArgumentException e) {
                    acceptCharacter = elements[1].trim();
                }

                if (initialStateName == null) initialStateName = StateName.CHARACTER_ACCEPT;
                if (finalStateName == null) finalStateName = StateName.CHARACTER_ACCEPT;
                if (acceptClass == null) acceptClass = CharacterClass.SPECIFIC;



                State initialState = new State(initialStateName, initialStatePrefix);
                State finalState = new State(finalStateName, finalStatePrefix);
                CharacterResolver accepts = new CharacterResolver(acceptClass, acceptCharacter);

                if (strategies.get(initialState) == null) {
                    strategies.put(initialState, new ResolutionStrategy());
                }

                strategies.get(initialState).addStrategy(accepts, finalState);

                line = br.readLine();
            }

            state = new State(StateName.CHARACTER_ACCEPT);
        } finally {
            br.close();
        }
    }

    public State tokenize(Character c) {
        ResolutionStrategy strategy = strategies.get(state);

        // If there is no resolution strategy, then we must be at a terminal state.
        if (strategy == null) {
            return null;
        }

        // We're going to mutate this set to help us select which character class to investigate.
        // Copying the set prevents us from accidentally mutating the underlying strategy by accident.
        Set<CharacterResolver> acceptableClasses = new HashSet<CharacterResolver>(strategy.getAcceptableCharacterClasses());

        while (acceptableClasses.size() > 0) {
            CharacterResolver mostSpecificClass = null;
            double priority = Float.POSITIVE_INFINITY;
            for (CharacterResolver characterResolver : acceptableClasses) {
                if (characterResolver.getPriority() < priority) {
                    priority = characterResolver.getPriority();
                    mostSpecificClass = characterResolver;
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

    public State getState() {
        return state;
    }

    public void reset() {
        state = new State(StateName.CHARACTER_ACCEPT);
    }

}
