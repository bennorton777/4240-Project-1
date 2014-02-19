package scanner;

/**
 * Created by ben on 2/18/14.
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
    @Override
    public int hashCode() {
        return (_characterClass.name() + _character).hashCode();
    }
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
