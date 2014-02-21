package parser;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RuleResolver {
    static final String FILENAME = "tiger_grammar.txt";

    private List<Rule> ruleList;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public RuleResolver() throws IOException, GrammarException {
        ruleList = new ArrayList<Rule>();
        first = new HashMap<String, Set<String>>();
        follow = new HashMap<String, Set<String>>();

        BufferedReader br = new BufferedReader(new FileReader(FILENAME));
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.indexOf('\t') < 0) throw new IOException("Could not parse rule: \""+line+"\"");
                String kv[] = line.split("\t");
                String symName = kv[0].replace("<","").replace(">","");
                String components[] = kv[1].split("#")[0].trim().split(" ");
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

        boolean changed = false;
        do {
            changed = false;
            for (Rule rule : ruleList) {
                Set<String> firstX = first.get(rule.getName());
                if (firstX == null) {
                    firstX = new HashSet<String>();
                    first.put(rule.getName(), firstX);
                }

                if (!firstX.contains("NULL")) {
                    if (rule.getSeq()[0].equals("NULL")) {
                        firstX.add("NULL");
                        changed = true;
                    } else {
                        boolean acc = true;
                        for (String sym : rule.getSeq()) {
                            if (!isNullable(sym)) {
                                acc = false;
                                break;
                            }
                        }
                        if (acc) {
                            firstX.add("NULL");
                            changed = true;
                        }
                    }
                }

                for (int i=0; i < rule.getSeq().length; i++) {
                    boolean acc = true;
                    int k;
                    for (k=0; k < i; k++) {
                        if (!isNullable(rule.getSeq()[k])) {
                            acc = false;
                            break;
                        }
                    }
                    if (acc) {
                        //FIRST[X] = FIRST[X] U FIRST[Yi]
                        String yi = rule.getSeq()[i];
                        if (yi.startsWith("<")) {
                            if (first.containsKey(yi.replace("<","").replace(">",""))) {
                                Set<String> firstYi = first.get(yi.replace("<","").replace(">",""));
                                if (!firstX.containsAll(firstYi)) changed = true;
                                firstX.addAll(firstYi);
                            }
                        } else if (!yi.equals("NULL")) {
                            if (!firstX.contains(yi)) changed = true;
                            firstX.add(yi);
                        }
                    }

                    /*acc = true;
                    for (k=i+1; k < rule.getSeq().length; k++) {
                        if (!isNullable(rule.getSeq()[k])) {
                            acc = false;
                            break;
                        }
                    }
                    if (acc) {
                        //FOLLOW[Yi] = FOLLOW[Yi] U FOLLOW[X]
                    }*/

                    /*for (int j=i+1; j < rule.getSeq().length; j++) {
                        acc = true;
                        for (k=i+1; k < j; k++) {
                            if (!isNullable(rule.getSeq()[k])) {
                                acc = false;
                                break;
                            }
                        }
                        if (acc) {
                            //FOLLOW[Yi] = FOLLOW[Yi] U FIRST[Yj]
                        }
                    }*/
                }
            }
        } while (changed);
    }

    private boolean isNullable(String sym) {
        if (!sym.startsWith("<") && !sym.equals("NULL")) return false;
        if (!first.containsKey(sym.replace("<","").replace(">",""))) return false;
        return first.get(sym.replace("<","").replace(">","")).contains("NULL");
    }

    public static void main(String[] args) throws IOException, GrammarException {
        RuleResolver r = new RuleResolver();
        System.out.println("Grammar is OK");
        //System.out.println("Nullables: "+r.nullable);
        System.out.println("First:");
        String[] syms = r.first.keySet().toArray(new String[0]);
        Arrays.sort(syms);

        for (String sym : syms) {
            System.out.println("\tFirst["+sym+"] = "+ r.first.get(sym));
        }
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
