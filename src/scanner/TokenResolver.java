package scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class is a stateful object that takes in a character stream via the {@link scanner.TokenResolver#tokenize(Character)} method.
 * It knows how to resolve characters based on a csv that describes state transitions.  This csv is read in the constructor.
 */
public class TokenResolver {

    // This represents the state transition graph.
    private Map<State, ResolutionStrategy> strategies;
    // Current state of the TokenResolver
    private State state;

    /** The constructor of this object formalizes the state transition graph based on a csv.
     * @throws IOException
     */
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


                /** This try/catch magic allows us to deal with states read in from the csv that are not explicitly
                * described in the {@link scanner.StateName} enum
                * */
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

    /**
     * This method changes the state of the TokenResolver based on the input character.
     * The rules that govern this transition are described in the csv read in the constructor.
     * @param c
     * @return
     */
    public State tokenize(Character c) {
        ResolutionStrategy strategy = strategies.get(state);

        // If there is no resolution strategy, then we must be at a terminal state.
        if (strategy == null) {
            return null;
        }

        // We're going to mutate this set to help us select which character class to investigate.
        // Copying the set prevents us from accidentally mutating the underlying strategy by accident.
        Set<CharacterResolver> acceptableClasses = new HashSet<CharacterResolver>(strategy.getAcceptableCharacterClasses());

        // We care about matching against the most specific transition rule that can be successfully applied considering
        // the current state and the input character.
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
                String displayText = state.getDisplayText();
                state = strategy.nextState(mostSpecificClass);
                state.setDisplayText(displayText + String.valueOf(c));
                return state;
            }
        }
        if ((state.getStateName() == StateName.CHARACTER_ACCEPT || state.getStateName() == StateName.ID) && CharacterClass.ALPHANUMERIC.resolve(c)) {
            State newState = new State(StateName.ID);
            newState.setDisplayText(state.getDisplayText()+String.valueOf(c));
            state = newState;
            return state;
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
