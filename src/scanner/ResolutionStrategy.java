package scanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ben on 2/9/14.
 */
public class ResolutionStrategy {

    private Map<CharacterClass, ScannerState> strategy;

    public ResolutionStrategy() {
        strategy = new HashMap<CharacterClass, ScannerState>();
    }

    public void addStrategy(CharacterClass c, ScannerState s) {
        strategy.put(c, s);
    }

    public ScannerState nextState(CharacterClass c) {
        return strategy.get(c);
    }

    public Set<CharacterClass> getAcceptableCharacterClasses() {
        return strategy.keySet();
    }
}
