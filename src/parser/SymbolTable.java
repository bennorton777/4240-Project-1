package parser;

import java.util.HashMap;

public class SymbolTable {

    private HashMap<String, Symbol> allSymbols = new HashMap<String,Symbol>();
    
    public void insertSymbol(Symbol sym) throws Exception{
        if(allSymbols.containsKey(sym.getKey()))
            throw new Exception("Symbol already exists!!");
        allSymbols.put(sym.getKey(), sym);
    }
    
    public void removeSymbol(String name, String scope){
        Symbol sym = new Symbol(name, scope);
        allSymbols.remove(sym.getKey());
    }
    
    public Symbol getSymbol(String name, String scope){
        Symbol sym = new Symbol(name, scope);
        return allSymbols.get(sym.getKey());
    }
    
    public void printTable(){
        for (Symbol sym : allSymbols.values()) {
            System.out.println(sym);
        }    
    }
}
