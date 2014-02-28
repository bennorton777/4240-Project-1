package parser;

import scanner.Token;

/**
 * Created by sjmitchell on 2/27/14.
 *
 * So several functions are missing.
 * For one, you can't remove children nodes.
 *
 * Also, the max number a children can have is seven-- this is very rudimentary and not at all efficient,
 * but it'll do for a stub. I'll come up with something a little more elegant when I have time.
 *
 * For now, there are getters and setters for all seven branches. Please utilize the branches in
 * ascending numerical order. I am not checking this! (Not putting that much work into an implementation I'm planning
 * to change.)
 */
public class ParseTreeNode {
    private int priority;
    private int size;
    private Token t;
    private ParseTreeNode one;
    private ParseTreeNode two;
    private ParseTreeNode three;
    private ParseTreeNode four;
    private ParseTreeNode five;
    private ParseTreeNode six;
    private ParseTreeNode seven;

    public ParseTreeNode (Token t) {
        this.t = t;
        this.size = 0;
    }

    public void setOne(ParseTreeNode one) {
        if (this.one == null)
            size++;
        this.one = one;
    }

    public void setTwo(ParseTreeNode two) {
        if (this.two == null)
            size++;
        this.two = two;
    }

    public void setThree(ParseTreeNode three) {
        if (this.three == null)
            size++;
        this.three = three;
    }

    public void setFour(ParseTreeNode four) {
        if (this.four == null)
            size++;
        this.four = four;
    }

    public void setFive(ParseTreeNode five) {
        if (this.five == null)
            size++;
        this.five = five;
    }

    public void setSix(ParseTreeNode six) {
        if (this.six == null)
            size++;
        this.six = six;
    }

    public void setSeven(ParseTreeNode seven) {
        if (this.seven == null)
            size++;
        this.seven = seven;
    }

    public void setT(Token t) {
        this.t = t;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Token getT() {
        return t;
    }

    public ParseTreeNode getOne() {
        return one;
    }

    public ParseTreeNode getTwo() {
        return two;
    }

    public ParseTreeNode getThree() {
        return three;
    }

    public ParseTreeNode getFour() {
        return four;
    }

    public ParseTreeNode getFive() {
        return five;
    }

    public ParseTreeNode getSix() {
        return six;
    }

    public ParseTreeNode getSeven() {
        return seven;
    }

    public int getPriority() {
        return priority;
    }

    public int getSize() {
        return size;
    }
}
