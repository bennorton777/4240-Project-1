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
