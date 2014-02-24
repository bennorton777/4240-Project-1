/**
 * TODO Token class needs to be separated from State class. 
 */

package scanner;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public static String prettyType(String type) {
        if (type.equals("STRLIT")) return "string";
        if (type.equals("INTLIT")) return "integer";
        if (type.equals("FUNC")) return "\"function\"";
        if (type.equals("ID")) return "identifier";
        if (type.equals("EQ")) return "\"=\"";
        if (type.equals("GREATER")) return "\">\"";
        if (type.equals("LESSER")) return "\"<\"";
        if (type.equals("AND")) return "\"&\"";
        if (type.equals("OR")) return "\"|\"";
        if (type.equals("GREATEREQ")) return "\">=\"";
        if (type.equals("LESSEREQ")) return "\"<=\"";
        if (type.equals("NEQ")) return "\"<>\"";
        if (type.equals("ASSIGN")) return "\":=\"";
        if (type.equals("COMMA")) return "\",\"";
        if (type.equals("COLON")) return "\":\"";
        if (type.equals("SEMI")) return "\";\"";
        if (type.equals("LPAREN")) return "\"(\"";
        if (type.equals("RPAREN")) return "\")\"";
        if (type.equals("RBRACK")) return "\"]\"";
        if (type.equals("LBRACK")) return "\"[\"";
        if (type.equals("PLUS")) return "\"+\"";
        if (type.equals("MINUS")) return "\"-\"";
        if (type.equals("MULT")) return "\"*\"";
        if (type.equals("DIV")) return "\"/\"";
        return "\""+type.toLowerCase()+"\"";
    }

    public static String prettyTypes(Collection<String> types) {
        StringBuilder sb = new StringBuilder();
        int i=0;
        for (String type : types) {
            if (i > 0 && i < types.size()-1) {
                sb.append(", ");
            } else if (i == types.size()-1) {
                if (types.size() > 2) {
                    sb.append(", or ");
                } else {
                    sb.append(" or ");
                }
            }
            sb.append(prettyType(type));
            i++;
        }
        return sb.toString();
    }
}
