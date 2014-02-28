package parser;

import scanner.Token;

import java.util.ArrayList;

/**
 * Created by sjmitchell on 2/27/14.
 *
 * So several functions are missing.
 * For one, you can't remove children nodes.
 *
 * An array list keeps track of the number of children a ParseTreeNode has. The index for children starts at 0.
 * (i.e. The left-most child would be index 0)
 *
 * For now, there are getters and setters for everything. Branches will be stored in the order that you add them.
 *
 * Feel free to make as few or many changes as you want.
 */
public class ParseTreeNode {
    private Token t;

    private ArrayList<ParseTreeNode> children;

    /**
     * Instantiates the ParseTreeNode with 0 children.
     * @param t
     */
    public ParseTreeNode (Token t) {
        this.t = t;
        this.children = new ArrayList<ParseTreeNode>();
    }

    /**
     * Adds a branch to the node
     * @param c The child to be added
     */
    public void addChild(Token c) {
        children.add(new ParseTreeNode(c));
    }

    /**
     * Sets the token of the node
     * @param t The node's new token
     */
    public void setT(Token t) {
        this.t = t;
    }

    /**
     * Gets the token of the node. Currently returns an empty token if it is a substitution node.
     * @return The token of the node
     */
    public Token getT() {
        return t;
    }

    /**
     * Gets the number of branches that the node has.
     * @return The number of branches
     */
    public int getSize() {
        return children.size();
    }

    /**
     * Gets the child i from the node
     * @param i the index of the child (for example, the left-most child would be i=0)
     * @return The node containing the child.
     */
    public ParseTreeNode getChild(int i) {
        return children.get(i);
    }
}
