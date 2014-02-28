package parser;

import scanner.Token;

/**
 * Created by sjmitchell on 2/27/14.
 */
public class ParseTreeStub {
    /**
     * This is a stub for the Parse Tree.
     * The parsed line that this tree represents is "type ArrayInt = array [100] of int;" from ex1.tiger
     * The tokens in the tree are "TYPE ID EQ ARRAY LBRACK INTLIT RBRACK OF ID SEMI"
     *
     * All "S" nodes have empty values for type/value and 0 for linenum/column.
     * In fact, /all/ of these are going to have 0 for line number/column. This can be added later.
     *
     * Please see the picture I uploaded to github to see how (I think) the parse tree should look like.
     */
    private ParseTreeNode head;

    public ParseTreeStub() {
        head = new ParseTreeNode(new Token("", "", 0, 0));
        head.setOne(new ParseTreeNode(new Token("", "", 0, 0)));
        head.setTwo(new ParseTreeNode(new Token("EQ", "=", 0, 0)));
        head.setThree(new ParseTreeNode(new Token("", "", 0, 0)));

        ParseTreeNode two = head.getOne();
        two.setOne(new ParseTreeNode(new Token("", "", 0, 0)));
        two.setTwo(new ParseTreeNode(new Token("", "", 0, 0)));

        ParseTreeNode three = two.getOne();
        three.setOne(new ParseTreeNode(new Token("TYPE", "type", 0, 0)));

        ParseTreeNode four = two.getTwo();
        four.setOne(new ParseTreeNode(new Token("ID", "ArrayInt", 0, 0)));

        ParseTreeNode five = head.getThree();
        five.setOne(new ParseTreeNode(new Token("", "", 0, 0)));
        five.setTwo(new ParseTreeNode(new Token("LBRACK", "[", 0, 0)));
        five.setThree(new ParseTreeNode(new Token("", "", 0, 0)));
        five.setFour(new ParseTreeNode(new Token("RBRACK", "]", 0, 0)));
        five.setFive(new ParseTreeNode(new Token("OF", "of", 0, 0)));
        five.setSix(new ParseTreeNode(new Token("", "", 0, 0)));
        five.setSeven(new ParseTreeNode(new Token("SEMI", ";", 0, 0)));

        ParseTreeNode six = five.getOne();
        six.setOne(new ParseTreeNode(new Token("ARRAY", "array", 0, 0)));

        ParseTreeNode seven = five.getThree();
        six.setOne(new ParseTreeNode(new Token("INT", "100", 0, 0)));

        ParseTreeNode eight = five.getSix();
        six.setOne(new ParseTreeNode(new Token("ID", "int", 0, 0)));

        System.out.println("          (S)");
        System.out.println("  (S)      =            (S)");
        System.out.println("(S) (S)         (S)  [  (S)  ]  (S)  ;");
        System.out.println("type ID        array    int     ID");
    }

    public static void main(String[] args) {
        ParseTreeStub b = new ParseTreeStub();
    }
}
