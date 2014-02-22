/**
 * TODO Token class needs to be separated from State class. 
 */

package scanner;


public class Token {
	private String type, value;

	public Token(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	@Override
    public String toString() {
        return "< " + type + " , " + value + " >";
    }
}
