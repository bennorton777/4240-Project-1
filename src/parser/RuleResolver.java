package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RuleResolver {
    static final String FILENAME = "tiger_grammar.txt";

    private List<Rule> ruleList;
    private List<String> nonTerminals;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;
    private Map<String, Rule> table;

    public RuleResolver() throws IOException, GrammarException {
        ruleList = new ArrayList<Rule>();
        first = new HashMap<String, Set<String>>();
        follow = new HashMap<String, Set<String>>();
        table = new HashMap<String, Rule>();

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

        nonTerminals = new ArrayList<String>();
        for (Rule rule : ruleList) {
            nonTerminals.add(rule.getName());
        }

        checkDefined();
        computeFirstFollow();
        fillTable();
    }

    public Rule getRule(String nonTerminal, String token) {
        return table.get(nonTerminal+","+token);
    }

    private void fillTable() throws GrammarException {
        for (Rule rule : ruleList) {
            //need to compute FIRST for this rule only
            Set<String> firstX = new HashSet<String>();
            boolean nullable = true;
            for (String sym : rule.getSeq()) {
                if (sym.startsWith("<")) {
                    firstX.addAll(first.get(sym.replace("<","").replace(">","")));
                } else {
                    firstX.add(sym);
                }
                if (!firstX.contains("NULL")) {
                    nullable = false;
                    break;
                }
            }
            firstX.remove("NULL");

            for (String tok : firstX) {
                String key = rule.getName()+","+tok;
                if (table.containsKey(key)) {
                    throw new GrammarException("Grammar is not LL(1): "
                    +key+" matches both\n\t"+table.get(key)+"\nand\n\t"+rule);
                } else {
                    table.put(key, rule);
                }
            }
            if (nullable) {
                for (String tok : follow.get("<"+rule.getName()+">")) {
                    String key = rule.getName()+","+tok;
                    if (table.containsKey(key)) {
                        throw new GrammarException("Grammar is not LL(1): "
                                +key+" matches both\n\t"+table.get(key)+"\nand\n\t"+rule);
                    } else {
                        table.put(key, rule);
                    }
                }
            }
        }
    }

    private void computeFirstFollow() {
        Set<String> eof = new HashSet<String>();
        eof.add("$");
        follow.put("<"+ruleList.get(0).getName()+">", eof);
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
                    String yi = rule.getSeq()[i];
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

                    acc = true;
                    for (k=i+1; k < rule.getSeq().length; k++) {
                        if (!isNullable(rule.getSeq()[k])) {
                            acc = false;
                            break;
                        }
                    }
                    if (acc) {
                        //FOLLOW[Yi] = FOLLOW[Yi] U FOLLOW[X]
                        Set<String> followYi = follow.get(yi);
                        if (followYi == null) {
                            followYi = new HashSet<String>();
                            follow.put(yi, followYi);
                        }
                        Set<String> followX = follow.get("<"+rule.getName()+">");
                        if (followX == null) {
                            followX = new HashSet<String>();
                            follow.put("<"+rule.getName()+">", followX);
                        }
                        if (!followYi.containsAll(followX)) changed = true;
                        followYi.addAll(followX);
                    }

                    for (int j=i+1; j < rule.getSeq().length; j++) {
                        acc = true;
                        for (k=i+1; k < j; k++) {
                            if (!isNullable(rule.getSeq()[k])) {
                                acc = false;
                                break;
                            }
                        }
                        if (acc) {
                            //FOLLOW[Yi] = FOLLOW[Yi] U FIRST[Yj]
                            Set<String> followYi = follow.get(yi);
                            if (followYi == null) {
                                followYi = new HashSet<String>();
                                follow.put(yi, followYi);
                            }
                            String yj = rule.getSeq()[j];
                            Set<String> firstYj;
                            if (yj.startsWith("<")) {
                                firstYj = first.get(yj.replace("<","").replace(">",""));
                                if (firstYj == null) {
                                    firstYj = new HashSet<String>();
                                    first.put(yj.replace("<","").replace(">",""), firstYj);
                                }
                                if (!followYi.containsAll(firstYj)) changed = true;
                                followYi.addAll(firstYj);
                            } else if (!yj.equals("NULL")) {
                                if (!followYi.contains(yj)) changed = true;
                                followYi.add(yj);
                            }
                        }
                    }
                }
            }
        } while (changed);

        for (Set<String> syms : follow.values()) {
            if (syms != null) syms.remove("NULL");
        }
    }

    private void checkDefined() throws GrammarException {
        for (Rule rule : ruleList) {
            for (String sym : rule.getSeq()) {
                if (sym.startsWith("<")) {
                    //it's a nonterminal
                    if (!nonTerminals.contains(sym.replace("<","").replace(">",""))) {
                        throw new GrammarException("Symbol " + sym.replace("<","").replace(">","") + " is not defined.");
                    }
                } else {
                    //it's a single token
                    //TODO validate
                }
            }
        }
    }

    private boolean isNullable(String sym) {
        if (!sym.startsWith("<") && !sym.equals("NULL")) return false;
        if (!first.containsKey(sym.replace("<","").replace(">",""))) return false;
        return first.get(sym.replace("<","").replace(">","")).contains("NULL");
    }

    public static void main(String[] args) throws IOException, GrammarException {
        RuleResolver r = new RuleResolver();
        System.out.println("Grammar is OK");

        System.out.println("First:");
        String[] syms = r.first.keySet().toArray(new String[0]);
        Arrays.sort(syms);
        for (String sym : syms) {
            System.out.println("\tFirst["+sym+"] = "+ r.first.get(sym));
        }
        System.out.println("Follow:");
        syms = r.follow.keySet().toArray(new String[0]);
        Arrays.sort(syms);
        for (String sym : syms) {
            System.out.println("\tFollow["+sym+"] = "+ r.follow.get(sym));
        }

        System.out.println("Table:");
        syms = r.table.keySet().toArray(new String[0]);
        Arrays.sort(syms);
        for (String sym : syms) {
            System.out.println("\t"+sym+":\t"+r.table.get(sym));
        }
    }

    static class GrammarException extends Exception {
        public GrammarException(String message) {
            super(message);
        }
    }
}
