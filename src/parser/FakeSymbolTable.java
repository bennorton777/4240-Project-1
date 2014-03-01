package parser;

import parser.Symbol.ContentType;
import parser.Symbol.SymbolTypes;
import scanner.Token;

/*
 * This is the probable symbol table that would be generated while parsing from ex1.tiger
 */

public class FakeSymbolTable {

    public static SymbolTable getFakeSymbolTable() throws Exception {
        
        SymbolTable fakeTable = new SymbolTable();
        Token[] tokens = new Token[20];

        // the tokens have the right order, line number and column number
        // but tokens that are not of symbol table's concern are not added
        tokens[0] = new Token("TYPE", "type", 3, 12);
        tokens[1] = new Token("ID", "ArrayInt", 3, 21);
        tokens[2] = new Token("EQ", "=", 3, 23);
        tokens[3] = new Token("ARRAY", "array", 3, 29);
        tokens[4] = new Token("VAR", "var", 4, 11);
        tokens[5] = new Token("ID", "X", 4, 13);
        tokens[6] = new Token("COMMA", ",", 4, 14);
        tokens[7] = new Token("ID", "Y", 4, 16);
        tokens[8] = new Token("COLON", ":", 4, 18);
        tokens[9] = new Token("ID", "ArrayInt", 4, 27);
        tokens[10] = new Token("VAR", "var", 5, 11);
        tokens[11] = new Token("ID", "i", 5, 13);
        tokens[12] = new Token("COMMA", ",", 5, 14);
        tokens[13] = new Token("ID", "sum", 5, 18);
        tokens[14] = new Token("COLON", ":", 5, 20);
        tokens[15] = new Token("ID", "int", 5, 23);

        Symbol sym = new Symbol(tokens[1], tokens[1].getValue(), "main", SymbolTypes.TYPE, ContentType.INT_ARRAY, 100);
        fakeTable.insertSymbol(sym);

        // resolution from ArrayInt type to corresponding type will be done in
        // the symbol table
        sym = new Symbol(tokens[5], tokens[5].getValue(), "main", Symbol.SymbolTypes.VARIABLE, ContentType.INT_ARRAY,
                100);
        fakeTable.insertSymbol(sym);

        sym = new Symbol(tokens[7], tokens[7].getValue(), "main", Symbol.SymbolTypes.VARIABLE, ContentType.INT_ARRAY,
                100);
        fakeTable.insertSymbol(sym);

        sym = new Symbol(tokens[11], tokens[11].getValue(), "main", Symbol.SymbolTypes.VARIABLE, ContentType.INTEGER);
        fakeTable.insertSymbol(sym);

        sym = new Symbol(tokens[13], tokens[13].getValue(), "main", Symbol.SymbolTypes.VARIABLE, ContentType.INTEGER);
        fakeTable.insertSymbol(sym);

        return fakeTable;
    }

    public static void main(String[] args) throws Exception{
        FakeSymbolTable.getFakeSymbolTable().printTable();
    }
}
