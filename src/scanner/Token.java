/**
 * TODO Token class needs to be separated from State class. 
 */

package scanner;


public class Token {
	private String type, value;
    private int line, column;

	public Token(String type, String value, int line, int column) {
		this.type = type;
		this.value = value;
        this.line = line;
        this.column = column;
	}

	public String getType() {
		return type;
	}

	public String getValue() { return value; }

    public int getLine() { return line; }

    public int getColumn() { return column; }
	
	@Override
    public String toString() {
        return "< " + type + " , " + value + " >";
    }
}
