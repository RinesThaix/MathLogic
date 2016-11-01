package ru.luvas.mathlogic.parser;

import lombok.Getter;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public enum ParsingLexeme {

    BRACKET_OPEN('('),
    BRACKET_CLOSE(')'),
    IMPLICATION("->"),
    OR('|'),
    AND('&'),
    NOT('!'),
    VARIABLE, END;
    
    @Getter
    private final char firstSymbol;
    
    @Getter
    private final String actualView;
    
    @Getter
    private final int length;
    
    ParsingLexeme() {
        this((char) 0);
    }
    
    ParsingLexeme(char c) {
        this(c + "");
    }
    
    ParsingLexeme(String actualView) {
        this.firstSymbol = actualView.charAt(0);
        this.actualView = actualView;
        this.length = actualView.length();
    }
    
    
}
