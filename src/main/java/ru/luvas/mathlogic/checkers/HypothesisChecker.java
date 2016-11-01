package ru.luvas.mathlogic.checkers;

import ru.luvas.mathlogic.Lexeme;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class HypothesisChecker implements LocalChecker {

    @Override
    public boolean check(Lexeme hypothesis, Lexeme provable) {
        if(hypothesis.getType() != provable.getType())
            return false;
        switch(hypothesis.getType()) {
            case VARIABLE:
                return hypothesis.getVariable().equals(provable.getVariable());
            case NOT:
                return check(hypothesis.getPrevious(), provable.getPrevious());
            default:
                return check(hypothesis.getPrevious(), provable.getPrevious()) &&
                        check(hypothesis.getNext(), provable.getNext());
        }
    }

}
