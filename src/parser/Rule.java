package parser;

public class Rule {
    private String name;
    private String seq[];
    public Rule(String name, String seq[]) {
        this.name = name;
        this.seq = seq;
    }

    public String getName() { return name; }
    public String[] getSeq() { return seq; }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" -> ");
        for (String sym : seq) {
            sb.append(sym);
            sb.append(' ');
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
