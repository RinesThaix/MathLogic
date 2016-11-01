package ru.luvas.mathlogic.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.luvas.mathlogic.Lexeme;
import ru.luvas.mathlogic.LexemeType;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Parser {
    
    private int index;
    private String expr;
    private ParsingLexeme pl;
    private Matcher variablesMatcher;

    public Lexeme parse(String expr) {
        this.index = 0;
        this.expr = expr;
        this.pl = null;
        constructMatcher();
        return parseExact(expr);
    }
    
    private void constructMatcher() {
        variablesMatcher = Pattern.compile("[A-Z]([A-Z]|[0-9])*").matcher(this.expr);
    }
    
    private Lexeme parseExact(String expr) {
        List<Lexeme> vars = new ArrayList<>(), operations = new ArrayList<>();
        while(true) {
            String partite = findPartite();
            switch(pl) {
                case BRACKET_OPEN:
                    vars.add(parseExact(expr));
                    break;
                case END:
                    if(operations.isEmpty())
                        return vars.get(0);
                case BRACKET_CLOSE:
                    return endThisGracefully(vars, operations);
                case VARIABLE:
                    vars.add(new Lexeme(partite));
                    break;
                case NOT:
                    operations.add(new Lexeme(LexemeType.NOT));
                    break;
                case AND:
                case OR:
                case IMPLICATION:
                    processOperation(LexemeType.valueOf(pl.name()), vars, operations);
                    break;
                default:
                    break;
            }
        }
    }
    
    private void processOperation(LexemeType type, List<Lexeme> vars, List<Lexeme> operations) {
        while(!operations.isEmpty() && operations.get(0).getType().hasHigherPriority(type)) {
            Lexeme operation = operations.remove(0);
            if(operation.getType() == LexemeType.NOT)
                vars.add(new Lexeme(operation, vars.remove(0)));
            else
                vars.add(new Lexeme(operation, vars.remove(0), vars.remove(0)));
        }
        operations.add(new Lexeme(type));
    }
    
    private Lexeme endThisGracefully(List<Lexeme> vars, List<Lexeme> operations) {
        while(!operations.isEmpty()) {
            Lexeme operation = operations.remove(0);
            if(operation.getType() == LexemeType.NOT)
                vars.add(new Lexeme(operation, vars.remove(0)));
            else
                vars.add(new Lexeme(operation, vars.remove(0), vars.remove(0)));
        }
        return vars.remove(0);
    }
    
    private String findPartite() {
        if(index + 1 > expr.length()) {
            pl = ParsingLexeme.END;
            return "";
        }
        char current = expr.charAt(index);
        if(current - 'A' >= 0 && 'Z' - current >= 0) {
            pl = ParsingLexeme.VARIABLE;
            String variable = "";
            if(variablesMatcher.find(index))
                variable = expr.substring(variablesMatcher.start(), variablesMatcher.end());
            index += variable.length();
            return variable;
        }
        for(ParsingLexeme plexeme : ParsingLexeme.values()) {
            if(current == plexeme.getFirstSymbol()) {
                index += plexeme.getLength();
                pl = plexeme;
                return plexeme.getActualView();
            }
        }
        return null;
    }
    
}
