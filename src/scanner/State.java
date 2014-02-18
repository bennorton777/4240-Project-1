package scanner;

/**
 * Created by ben on 2/18/14.
 */
public class State {
    private String _prefix;
    private StateName _stateName;
    public State (StateName name) {
        this(name, "");
    }
    public State(StateName name, String prefix) {
        _prefix = prefix;
        _stateName = name;
    }
    @Override
    public int hashCode() {
        return (_stateName.name()+_prefix).hashCode();
    }
    @Override
    public boolean equals(Object o){
        try {
            State other = (State) o;
            return (other.getStateName() == getStateName()) && (other.getPrefix().equals(_prefix));
        }
        catch (Exception E){
            throw new IllegalArgumentException("TODO When calling State.equals on something that isn't a state, the application should not die horribly.");
        }
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
}
