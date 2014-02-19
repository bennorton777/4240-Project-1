package scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ben on 2/9/14.
 */
public class ResolutionStrategy {

    private Map<CharacterResolver, State> strategy;

    public ResolutionStrategy() {
        strategy = new HashMap<CharacterResolver, State>();
    }

    public void addStrategy(CharacterResolver c, State s) {
        strategy.put(c, s);
    }

    public State nextState(CharacterResolver c) {
        return strategy.get(c);
    }

    public Set<CharacterResolver> getAcceptableCharacterClasses() {
        return strategy.keySet();
    }
}
