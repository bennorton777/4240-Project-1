package scanner;

/**
 * Created by ben on 2/9/14.
 */
public enum CharacterClass {

    SPACE {
        public int getPriority() {
            return 0;
        }
        public boolean resolve(Character c) {
            return " ".equals(String.valueOf(c)) || "\n".equals(String.valueOf(c)) || "\t".equals(String.valueOf(c));
        }
    },
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
    SPECIFIC {
        public int getPriority() {
            return 0;
        }
        //TODO this method should never be called.  It should, however, have a more sensible response than just returning false.
        public boolean resolve(Character c) {
            return false;
        }
    };


    public abstract int getPriority();
    public abstract boolean resolve(Character c);
}
