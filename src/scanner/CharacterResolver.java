package scanner;

/**
 * This class is a wrapper around {@link scanner.CharacterClass} that allows us to consider either a {@link scanner.CharacterClass}
 * or a specific character, as the case may be.  Of particular interest here are the custom equals and hashcode methods,
 * which allow this object to be used in Maps appropriately.
 * @see CharacterResolver#hashCode()
 * @see CharacterResolver#equals(Object o)
 */
public class CharacterResolver {

    private CharacterClass _characterClass;
    private String _character;

    public CharacterResolver(CharacterClass acceptClass, String character) {
        _characterClass = acceptClass;
        _character = character;
    }

    public CharacterResolver(CharacterClass characterClass) {
        _characterClass = characterClass;
        _character = "";
    }

    public CharacterResolver(String character) {
        _character = character;
    }

    /**
     * Necessary for this object to function correctly in maps.
     * @return
     */
    @Override
    public int hashCode() {
        return (_characterClass.name() + _character).hashCode();
    }

    /**
     * Necessary for this object to function correctly in maps.
     * @param o
     * @return
     */
    @Override
    public boolean equals (Object o) {
        try {
            CharacterResolver other = (CharacterResolver) o;
            return other.getCharacterClass().name().equals(_characterClass.name()) &&
                    other.getCharacter().equals(_character);
        }
        catch(Exception e) {
            return false;
        }
    }

    public int getPriority() {
        return _characterClass.getPriority();
    }

    /**
     * Either match against a {@link scanner.CharacterClass} or against a specific character.
     * @param c
     * @return
     */
    public boolean resolve(Character c) {
        if (_characterClass != CharacterClass.SPECIFIC) {
            return _characterClass.resolve(c);
        }
        else {
            return _character.equals(String.valueOf(c));
        }
    }

    public String getCharacter() {


        return _character;
    }

    public CharacterClass getCharacterClass() {
        return _characterClass;
    }
}
