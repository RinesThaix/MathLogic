package ru.luvas.mathlogic.checkers;

import java.util.HashMap;
import java.util.Map;
import ru.luvas.mathlogic.Lexeme;
import ru.luvas.mathlogic.LexemeType;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class AxiomsChecker implements LocalChecker {
    
    private final Map<String, Lexeme> same = new HashMap<>();

    @Override
    public boolean check(Lexeme hypothesis, Lexeme provable) {
        if(hypothesis.getType() == LexemeType.VARIABLE) {
            String var = hypothesis.getVariable();
            Lexeme same = this.same.get(var);
            if(same == null) {
                this.same.put(var, provable);
                return true;
            }
            return same.isTheSame(provable);
        }
        if(hypothesis.getType() != provable.getType())
            return false;
        switch(hypothesis.getType()) {
            case NOT:
                return check(hypothesis.getPrevious(), provable.getPrevious());
            default:
                return check(hypothesis.getPrevious(), provable.getPrevious()) &&
                        check(hypothesis.getNext(), provable.getNext());
        }
    }

}
