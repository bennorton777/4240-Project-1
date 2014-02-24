package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RuleResolver {

	public final static String EOF_SYMBOL = "$";
	public final static String NULL_SYMBOL = "NULL";
	public final String START_SYMBOL;

	private static final String RULES_FILENAME = "tiger_grammar.txt";

	private List<Rule> ruleList;
	private Set<String> nonTerminals;
	private Map<String, Set<String>> first;
	private Map<String, Set<String>> follow;
	private Map<String, Rule> table;

	public RuleResolver() throws IOException, GrammarException {

		ruleList = new ArrayList<Rule>();
		first = new HashMap<String, Set<String>>();
		follow = new HashMap<String, Set<String>>();
		table = new HashMap<String, Rule>();

		BufferedReader br = new BufferedReader(new FileReader(RULES_FILENAME));
		try {
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				if (line.isEmpty() || line.startsWith("#"))
					continue;
				if (line.indexOf('\t') < 0)
					throw new IOException("Could not parse rule: \"" + line
							+ "\"");
				String kv[] = line.split("\t");
				String symName = kv[0];
				String components[] = kv[1].split("#")[0].trim().split(" ");
				ruleList.add(new Rule(symName, components));
			}
		} finally {
			br.close();
		}

        START_SYMBOL = ruleList.get(0).getName();

        nonTerminals = new HashSet<String>();
		for (Rule rule : ruleList) {
			nonTerminals.add(rule.getName());
		}

		checkDefined();
		computeFirstFollow();
		fillTable();
	}

    public Set<String> getFirstSet(String nonTerminal) {
        return first.get(nonTerminal);
    }

	private void fillTable() throws GrammarException {
		for (Rule rule : ruleList) {
			// need to compute FIRST for this rule only
			Set<String> firstX = new HashSet<String>();
            Set<String> firstY;
			boolean nullable = true;
			for (String sym : rule.getSeq()) {
				if (sym.startsWith("<")) {
					firstY = first.get(sym);
                    firstX.addAll(firstY);
                    if (!firstY.contains(NULL_SYMBOL)) {
                        nullable = false;
                        break;
                    }
				} else {
					firstX.add(sym);
                    if (!sym.equals(NULL_SYMBOL)) {
                        nullable = false;
                        break;
                    }
				}
			}
			firstX.remove(NULL_SYMBOL);

			for (String tok : firstX) {
				String key = rule.getName() + "," + tok;
				if (table.containsKey(key)) {
					throw new GrammarException("Grammar is not LL(1): " + key
							+ " matches both\n\t" + table.get(key)
							+ "\nand\n\t" + rule);
				} else {
					table.put(key, rule);
				}
			}
			if (nullable) {
				for (String tok : follow.get(rule.getName())) {
					String key = rule.getName() + "," + tok;
					if (table.containsKey(key)) {
						throw new GrammarException("Grammar is not LL(1): "
								+ key + " matches both\n\t" + table.get(key)
								+ "\nand\n\t" + rule);
					} else {
						table.put(key, rule);
					}
				}
			}
		}
	}

	private void computeFirstFollow() {
		Set<String> eof = new HashSet<String>();
		eof.add(EOF_SYMBOL);
		follow.put(START_SYMBOL, eof);
		boolean changed = false;
		do {
			changed = false;
			for (Rule rule : ruleList) {
				Set<String> firstX = first.get(rule.getName());
				if (firstX == null) {
					firstX = new HashSet<String>();
					first.put(rule.getName(), firstX);
				}

				if (!firstX.contains(NULL_SYMBOL)) {
					if (rule.getSeq()[0].equals(NULL_SYMBOL)) {
						firstX.add(NULL_SYMBOL);
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
							firstX.add(NULL_SYMBOL);
							changed = true;
						}
					}
				}

				for (int i = 0; i < rule.getSeq().length; i++) {
					String yi = rule.getSeq()[i];
					boolean acc = true;
					int k;
					for (k = 0; k < i; k++) {
						if (!isNullable(rule.getSeq()[k])) {
							acc = false;
							break;
						}
					}
					if (acc) {
						// FIRST[X] = FIRST[X] U FIRST[Yi]-NULL
						if (yi.startsWith("<")) {
							if (first.containsKey(yi)) {
								Set<String> firstYi = new HashSet<String>(first.get(yi));
                                firstYi.remove(NULL_SYMBOL);
								if (!firstX.containsAll(firstYi))
									changed = true;
								firstX.addAll(firstYi);
							}
						} else if (!yi.equals(NULL_SYMBOL)) {
							if (!firstX.contains(yi))
								changed = true;
							firstX.add(yi);
						}
					}

					acc = true;
					for (k = i + 1; k < rule.getSeq().length; k++) {
						if (!isNullable(rule.getSeq()[k])) {
							acc = false;
							break;
						}
					}
					if (acc) {
						// FOLLOW[Yi] = FOLLOW[Yi] U FOLLOW[X]-NULL
						Set<String> followYi = follow.get(yi);
						if (followYi == null) {
							followYi = new HashSet<String>();
							follow.put(yi, followYi);
						}
                        if (!follow.containsKey(rule.getName())) {
                            follow.put(rule.getName(), new HashSet<String>());
                        }
						Set<String> followX = new HashSet<String>(follow.get(rule.getName()));
                        followX.remove(NULL_SYMBOL);
						if (!followYi.containsAll(followX))
							changed = true;
						followYi.addAll(followX);
					}

					for (int j = i + 1; j < rule.getSeq().length; j++) {
						acc = true;
						for (k = i + 1; k < j; k++) {
							if (!isNullable(rule.getSeq()[k])) {
								acc = false;
								break;
							}
						}
						if (acc) {
							// FOLLOW[Yi] = FOLLOW[Yi] U FIRST[Yj]-NULL
							Set<String> followYi = follow.get(yi);
							if (followYi == null) {
								followYi = new HashSet<String>();
								follow.put(yi, followYi);
							}
							String yj = rule.getSeq()[j];
							Set<String> firstYj;
							if (yj.startsWith("<")) {
                                if (!first.containsKey(yj)) {
                                    first.put(yj, new HashSet<String>());
                                }
								firstYj = new HashSet<String>(first.get(yj));
                                firstYj.remove(NULL_SYMBOL);
								if (!followYi.containsAll(firstYj))
									changed = true;
								followYi.addAll(firstYj);
							} else if (!yj.equals(NULL_SYMBOL)) {
								if (!followYi.contains(yj))
									changed = true;
								followYi.add(yj);
							}
						}
					}
				}
			}
		} while (changed);

		for (Set<String> syms : follow.values()) {
			if (syms != null)
				syms.remove(NULL_SYMBOL);
		}
	}

	private void checkDefined() throws GrammarException {
		for (Rule rule : ruleList) {
			for (String sym : rule.getSeq()) {
				if (sym.startsWith("<")) {
					// it's a nonterminal
					if (!nonTerminals.contains(sym)) {
						throw new GrammarException("Symbol "
								+ sym + " is not defined.");
					}
				} else {
					// it's a single token
					// TODO validate
				}
			}
		}
	}

	private boolean isNullable(String sym) {
		if (!sym.startsWith("<") && !sym.equals(NULL_SYMBOL))
			return false;
		if (!first.containsKey(sym))
			return false;
		return first.get(sym).contains(NULL_SYMBOL);
	}

	static class GrammarException extends Exception {
		public GrammarException(String message) {
			super(message);
		}
	}

	public Rule getRule(String nonTerminal, String token) {
		return table.get(nonTerminal + "," + token);
	}

	public boolean isNonTerminal(String top) {
		return nonTerminals.contains(top);
	}
	
	public void printFollowSets() {
		String[] syms;
		System.out.println("Follow:");
		syms = follow.keySet().toArray(new String[0]);
		Arrays.sort(syms);
		for (String sym : syms) {
			System.out.println("\tFollow[" + sym + "] = " + follow.get(sym));
		}
	}

	public void printFirstSets() {
		System.out.println("First:");
		String[] syms = first.keySet().toArray(new String[0]);
		Arrays.sort(syms);
		for (String sym : syms) {
			System.out.println("\tFirst[" + sym + "] = " + first.get(sym));
		}
	}

	public void printTable() {
		String[] syms;
		System.out.println("Table:");
		syms = table.keySet().toArray(new String[0]);
		Arrays.sort(syms);
		for (String sym : syms) {
			System.out.println("\t" + sym + ":\t" + table.get(sym));
		}
	}
	
	public static void main(String[] args) throws IOException, GrammarException {
		RuleResolver r = new RuleResolver();
		System.out.println("Grammar is OK");
		r.printFirstSets();
		r.printFollowSets();
		r.printTable();
	}

	
}
