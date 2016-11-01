package ru.luvas.mathlogic;

import lombok.Data;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public class Lexeme {
    
    private final static int HASH_PRIME = 31;
    private final static int HASH_PRIME2 = HASH_PRIME * HASH_PRIME;
    private final static int HASH_PRIME3 = HASH_PRIME2 * HASH_PRIME;
    
    private final LexemeType type;
    
    private final String variable;
    
    private final int hash;

    private final Lexeme previous, next;
    
    public Lexeme() {
        this.type = LexemeType.VARIABLE;
        this.variable = "";
        this.hash = 1;
        this.previous = this.next = null;
    }
    
    public Lexeme(String variable) {
        this.type = LexemeType.VARIABLE;
        this.variable = variable;
        this.hash = 2 + variable.hashCode();
        this.previous = this.next = null;
    }
    
    public Lexeme(LexemeType type) {
        this.type = type;
        this.variable = "";
        this.hash = 3 + type.ordinal();
        this.previous = this.next = null;
    }
    
    public Lexeme(Lexeme operation, Lexeme variable) {
        this.variable = "";
        this.type = operation.getType();
        this.hash = HASH_PRIME3 * variable.hash + HASH_PRIME * this.type.ordinal();
        this.previous = variable;
        this.next = null;
    }
    
    public Lexeme(Lexeme operation, Lexeme previous, Lexeme next) {
        this.variable = "";
        this.type = operation.getType();
        this.hash = HASH_PRIME3 * previous.hash + HASH_PRIME2 * next.hash + HASH_PRIME * this.type.ordinal();
        this.previous = previous;
        this.next = next;
    }
    
    public boolean isTheSame(Lexeme l) {
        return areTheSame(this, l);
    }
    
    @Override
    public int hashCode() {
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Lexeme))
            return false;
        Lexeme l = (Lexeme) o;
        if(this.hash != l.hash)
            return false;
        return checkForEquality(this.previous, l.previous) && checkForEquality(this.next, l.next);
    }
    
    private boolean checkForEquality(Object a, Object b) {
        if(a == null)
            return b == null;
        if(b == null)
            return false;
        return a.equals(b);
    }
    
    public static boolean areTheSame(Lexeme l1, Lexeme l2) {
        if(l1.type != l2.type)
            return false;
        switch(l1.type) {
            case VARIABLE:
                return l1.variable.equals(l2.variable);
            case NOT:
                return areTheSame(l1.previous, l2.previous);
            default:
                return areTheSame(l1.previous, l2.previous) && areTheSame(l1.next, l2.next);
        }
    }
    
}
