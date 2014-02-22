package scanner;

/**
 * This data structure is responsible for containing data about the current state of the application.  States also serve
 * as tokens, as referred to in the project writeup.
 */
public class State {

    // Prefix is a field that corresponds to the text previously read.  It is used only in CHARACTER_ACCEPT states.
    // Two states with different prefixes are not considered equivalent.
    private String _prefix;
    // This corresponds to the tokens referred to in the project writeup.
    private StateName _stateName;
    // DisplayText is a field that corresponds to the text previously read.  It is used in all states.
    // Two states with different displayTexts can still be considered equivalent, as long as their prefixes and names
    // match
    private String _displayText;

    public State (StateName name) {
        this(name, "");
    }

    public State(StateName name, String prefix) {
        _prefix = prefix;
        _stateName = name;
        _displayText = "";
    }

    public State(State state) {
        _prefix = state.getPrefix();
        _stateName = state.getStateName();
        _displayText = state.getDisplayText();
    }

    /**
     * Necessary for appropriate behavior in maps.
     * @return
     */
    @Override
    public int hashCode() {
        return (_stateName.name()+_prefix).hashCode();
    }

    /**
     * Necessary for appropriate behavior in maps.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        try {
            State other = (State) o;
            return (other.getStateName() == getStateName()) && (other.getPrefix().equals(_prefix));
        }
        catch (Exception E){
            return false;
        }
    }

    @Override
    public String toString() {
        return "<" + _stateName.name() + ", " + _displayText.trim() + ">";
    }

    public String getPrefix() {
        return _prefix;
    }

    public void setPrefix(String _prefix) {
        this._prefix = _prefix;
    }

    public StateName getStateName() {
        return _stateName;
    }

    public void setStateName(StateName _stateName) {
        this._stateName = _stateName;
    }

    public String getDisplayText() {
        return _displayText;
    }

    public void setDisplayText(String _displayText) {
        this._displayText = _displayText;
    }

}
