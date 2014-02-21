package parser;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuleResolver {
    static final String FILENAME = "tiger_grammar.txt";

    private List<Rule> ruleList;
    public RuleResolver() throws IOException, GrammarException {
        ruleList = new ArrayList<Rule>();
        BufferedReader br = new BufferedReader(new FileReader(FILENAME));
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.indexOf('\t') < 0) throw new IOException("Could not parse rule: \""+line+"\"");
                String kv[] = line.split("\t");
                String symName = kv[0].replace("<","").replace(">","");
                String components[] = kv[1].split(" ");
                ruleList.add(new Rule(symName, components));
            }
        } finally {
            br.close();
        }

        //check that all syms are defined
        List<String> names = new ArrayList<String>();
        for (Rule rule : ruleList) {
            names.add(rule.getName());
        }
        for (Rule rule : ruleList) {
            for (String sym : rule.getSeq()) {
                if (sym.startsWith("<")) {
                    //it's a nonterminal
                    if (!names.contains(sym.replace("<","").replace(">",""))) {
                        throw new GrammarException("Symbol " + sym.replace("<","").replace(">","") + " is not defined.");
                    }
                } else {
                    //it's a single token
                    //TODO validate
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, GrammarException {
        RuleResolver r = new RuleResolver();
        System.out.println("Grammar is OK");
    }
    static class Rule {
        private String name;
        private String seq[];
        public Rule(String name, String seq[]) {
            this.name = name;
            this.seq = seq;
        }

        public String getName() { return name; }
        public String[] getSeq() { return seq; }
    }

    static class GrammarException extends Exception {
        public GrammarException(String message) {
            super(message);
        }
    }
}
