package scanner;

/**
 * Created by ben on 2/9/14.
 */
public enum CharacterClass {

    LETTER {
        public boolean resolve (Character c) {
            return Character.isLetter(c);
        }
        public int getPriority() {
            return 1;
        }
    },
    DIGIT {
        public boolean resolve (Character c) {
            return Character.isDigit(c);
        }
        public int getPriority() {
            return 1;
        }
    },
    ALPHANUMERIC {
        public boolean resolve (Character c) {
            return LETTER.resolve(c) || DIGIT.resolve(c);
        }
        public int getPriority() {
            return 2;
        }
    },
    ANY {
        public boolean resolve(Character c) {
            return true;
        }
        public int getPriority() {
            return 3;
        }
    },
    COMMA {
        public boolean resolve(Character c) {
            return c == ",".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    COLON {
        public boolean resolve(Character c) {
            return c == ":".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    SEMICOLON {
        public boolean resolve(Character c) {
            return c == ";".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    LEFT_PARENTHESIS {
        public boolean resolve(Character c) {
            return c == "(".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    RIGHT_PARENTHESIS {
        public boolean resolve(Character c) {
            return c == ")".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    LEFT_BRACKET {
        public boolean resolve(Character c) {
            return c == "[".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    RIGHT_BRACKET {
        public boolean resolve(Character c) {
            return c == "]".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    LEFT_BRACE {
        public boolean resolve(Character c) {
            return c == "{".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    RIGHT_BRACE {
        public boolean resolve(Character c) {
            return c == "}".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    PERIOD {
        public boolean resolve(Character c) {
            return c == ".".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    PLUS_SIGN {
        public boolean resolve(Character c) {
            return c == "+".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    MINUS_SIGN {
        public boolean resolve(Character c) {
            return c == "-".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    MULTIPLICATION_SIGN {
        public boolean resolve(Character c) {
            return c == "*".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    DIVISION_SIGN {
        public boolean resolve(Character c) {
            return c == "/".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    EQUALS {
        public boolean resolve(Character c) {
            return c == "=".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    GREATER_THAN_SIGN {
        public boolean resolve(Character c) {
            return c == ">".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    LESS_THAN_SIGN {
        public boolean resolve(Character c) {
            return c == "<".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    AMPERSAND {
        public boolean resolve(Character c) {
            return c == "&".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    QUOTE {
        public boolean resolve(Character c) {
            return c == "\"".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    BACKSLASH {
        public boolean resolve(Character c) {
            return c == "\\".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    SPACE {
        public boolean resolve(Character c) {
            return c == " ".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    CARET {
        public boolean resolve(Character c) {
            return c == "^".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    BAR {
        public boolean resolve(Character c) {
            return c == "|".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    A {
        public boolean resolve(Character c) {
            return c == "a".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    B {
        public boolean resolve(Character c) {
            return c == "b".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    C {
        public boolean resolve(Character c) {
            return c == "c".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    D {
        public boolean resolve(Character c) {
            return c == "d".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    E {
        public boolean resolve(Character c) {
            return c == "e".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    F {
        public boolean resolve(Character c) {
            return c == "f".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    G {
        public boolean resolve(Character c) {
            return c == "g".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    H {
        public boolean resolve(Character c) {
            return c == "h".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    I {
        public boolean resolve(Character c) {
            return c == "i".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    K {
        public boolean resolve(Character c) {
            return c == "k".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    L {
        public boolean resolve(Character c) {
            return c == "l".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    N {
        public boolean resolve(Character c) {
            return c == "n".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    O {
        public boolean resolve(Character c) {
            return c == "o".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    P {
        public boolean resolve(Character c) {
            return c == "p".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    R {
        public boolean resolve(Character c) {
            return c == "r".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    S {
        public boolean resolve(Character c) {
            return c == "s".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    T {
        public boolean resolve(Character c) {
            return c == "t".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    U {
        public boolean resolve(Character c) {
            return c == "u".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    V {
        public boolean resolve(Character c) {
            return c == "v".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    W {
        public boolean resolve(Character c) {
            return c == "w".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    },
    Y {
        public boolean resolve(Character c) {
            return c == "y".charAt(0);
        }
        public int getPriority() {
            return 0;
        }
    };


    public abstract int getPriority();
    public abstract boolean resolve(Character c);
}
