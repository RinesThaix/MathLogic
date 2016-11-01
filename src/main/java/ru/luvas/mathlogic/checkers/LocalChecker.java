package ru.luvas.mathlogic.checkers;

import ru.luvas.mathlogic.Lexeme;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public interface LocalChecker {

    boolean check(Lexeme hypothesis, Lexeme provable);
    
}
