package scanner;

/**
 * CharacterClass is responsible for enumerating all of the different groups of characters that may be referred to in
 * the state transition csv.  If the input entry for a particular transition rule cannot be found in the enum, it is
 * assumed to refer to a specific character, and will receive the {@link CharacterClass#SPECIFIC} designation.
 * For more information on how that case is handled, see {@link scanner.CharacterResolver}, which is the primary consumer
 * of this object type.
 * CharacterClasses also know what priority they have relative to other CharacterClasses.  This allows a consumer of
 * CharacterClasses to identify the most specific transition type allowed by a given input character.
 */
public enum CharacterClass {

    SPACE {
        public int getPriority() {
            return 0;
        }
        public boolean resolve(Character c) {
            return " ".equals(String.valueOf(c)) || "\n".equals(String.valueOf(c)) || "\t".equals(String.valueOf(c)) || "\r".equals(String.valueOf(c));
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
    ANY_STRING {
        public boolean resolve(Character c) {
            if (!c.equals("\\") || !c.equals("\""))
                return true;
            return false;
        }
        public int getPriority() {
            return 2;
        }
    },
    ESCAPE_CHAR {
        public boolean resolve(Character c) {
            if (!c.equals('\\') || !c.equals('"'))
                return true;
            return false;
        }
        public int getPriority() {
            return 2;
        }
    },
    ID_CHAR {
        public boolean resolve(Character c) {
            return (LETTER.resolve(c) || DIGIT.resolve(c) || "_".equals(String.valueOf(c)));
        }
        public int getPriority() {
            return 0;
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
