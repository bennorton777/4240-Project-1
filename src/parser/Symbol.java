package parser;

import scanner.Token;

public class Symbol {

    public enum SymbolTypes {
        VARIABLE, TYPE, FUNCTION, PARAM_BY_VAL, PARAM_BY_REF
    }

    public enum ContentType {
        INTEGER, STRING, INT_ARRAY, STRING_ARRAY
    }

    private Token eqivalentToken;
    // Scope is the procedure in which the symbol is defined
    private String name, scope;
    private SymbolTypes symType;
    // In case of functions, content type is the return value
    private ContentType contentType;
    // TODO This attribute can be removed
    // if we are not doing bounds checking or if this detail is the
    // responsibility of the semantic checker
    // In case of functions, this represents number of arguments
    private int count = -1;

    // TODO: I am not sure if more parameters need to be added. I guess the
    // semantic checking part should decide that.

    public Symbol(String name, String scope) {
        this.name = name;
        this.scope = scope;
    }

    public Symbol(Token tok, String name, String scope, SymbolTypes symType, ContentType contentType, int count)
            throws Exception {
        this.eqivalentToken = tok;
        this.name = name;
        this.symType = symType;
        this.contentType = contentType;
        this.scope = scope;

        if (count!=-1 && !hasCount())
            throw new Exception("Symbol should not have a count value!!");

        this.count = count;
    }

    public Symbol(Token tok, String name, String scope, SymbolTypes symType, ContentType contentType) throws Exception {
        this(tok, name, scope, symType, contentType, -1);
        if (hasCount())
            throw new Exception("Symbol should not have a count value");
    }

    public String getKey() {
        return name + scope;
    }

    public boolean hasCount() {
        return ((symType == SymbolTypes.FUNCTION) || ((symType == SymbolTypes.VARIABLE || symType == SymbolTypes.TYPE) && (contentType == ContentType.INT_ARRAY || contentType == ContentType.STRING_ARRAY)));
    }

    public boolean isArray() {
        return (symType == SymbolTypes.VARIABLE && (contentType == ContentType.INT_ARRAY || contentType == ContentType.STRING_ARRAY));
    }

    public boolean isParameter() {
        return (symType == SymbolTypes.PARAM_BY_REF || symType == SymbolTypes.PARAM_BY_VAL);
    }

    public boolean isFunction() {
        return symType == SymbolTypes.FUNCTION;
    }

    @Override
    public String toString() {
        return name + "," + scope + "," + symType + "," + contentType + "," + count + "," + eqivalentToken;
    }

    @Override
    public int hashCode() {
        String key = name + scope;
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Symbol other = (Symbol) obj;
        if (this.name.equals(other.name) && this.scope.equals(other.scope))
            return true;
        return false;
    }

    public Token getEqivalentToken() {
        return eqivalentToken;
    }

    public void setEqivalentToken(Token eqivalentToken) {
        this.eqivalentToken = eqivalentToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolTypes getSymType() {
        return symType;
    }

    public void setSymType(SymbolTypes type) {
        this.symType = type;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType type) {
        this.contentType = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
