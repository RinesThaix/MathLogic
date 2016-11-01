package ru.luvas.mathlogic;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public enum LexemeType {

    VARIABLE, IMPLICATION, OR, AND, NOT;
    
    public boolean hasHigherPriority(LexemeType lt) {
        return ordinal() >= lt.ordinal();
    }
    
}
