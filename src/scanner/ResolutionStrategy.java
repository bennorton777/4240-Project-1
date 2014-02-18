package scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ben on 2/9/14.
 */
public class ResolutionStrategy {

    private Map<CharacterClass, State> strategy;

    public ResolutionStrategy() {
        strategy = new HashMap<CharacterClass, State>();
    }

    public void addStrategy(CharacterClass c, State s) {
        strategy.put(c, s);
    }

    public State nextState(CharacterClass c) {
        return strategy.get(c);
    }

    public Set<CharacterClass> getAcceptableCharacterClasses() {
        return strategy.keySet();
    }
}
