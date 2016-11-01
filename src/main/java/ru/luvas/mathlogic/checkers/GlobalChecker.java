package ru.luvas.mathlogic.checkers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ru.luvas.mathlogic.Axioms;
import ru.luvas.mathlogic.Lexeme;
import ru.luvas.mathlogic.LexemeType;
import ru.luvas.mathlogic.parser.Parser;
import ru.luvas.mathlogic.utils.UtilChat;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class GlobalChecker {
    
    private BufferedReader reader;
    private PrintWriter pw;
    
    
    private List<String> hypothesis, expressions;
    
    private List<Lexeme> lexemeAxioms, lexemeHypothesis;
    
    private Map<Integer, Lexeme> lexemesByIds;
    private Map<Lexeme, Integer> idsByLexemes;
    
    private Map<Lexeme, Set<Integer>> implications;
    
    private Parser parser;
    
    public void check(String input, String output) {
        UtilChat.log("Ready to check expressions from %s into %s", input, output);
        clearStoragedData();
        UtilChat.log("Reading input file..");
        long fulltime = System.currentTimeMillis();
        readInputFile(input, output);
        UtilChat.log("Parsing lexems..");
        long exacttime = System.currentTimeMillis();
        parseLexems();
        UtilChat.log("Proving..");
        prove();
        long current = System.currentTimeMillis();
        UtilChat.log("Done! Some time statistics:");
        UtilChat.log("Time passed from the beginning: %dms", current - fulltime);
        UtilChat.log("Time passed after reading input file: %dms", current - exacttime);
    }
    
    private void clearStoragedData() {
        expressions = new ArrayList<>();
        lexemesByIds = new HashMap<>();
        idsByLexemes = new HashMap<>();
        implications = new HashMap<>();
        parser = new Parser();
    }
    
    private void readInputFile(String inputName, String outputName) {
        try {
            File input = new File(inputName), output = new File(outputName);
            if(!input.exists()) {
                UtilChat.log("There's no input file! Shutting down!");
                return;
            }
            if(!output.exists())
                output.createNewFile();
            reader = new BufferedReader(new FileReader(input));
            pw = new PrintWriter(new FileWriter(output));
            String readen;
            while((readen = reader.readLine()) != null && readen.isEmpty());
            if(readen == null) {
                UtilChat.log("There's nothing to read! Shutting down!");
                return;
            }
            readen = readen.replaceAll(" ", "");
            pw.println(readen);
            hypothesis = new ArrayList<>(Arrays.asList(readen.split("\\,")));
            String[] split = hypothesis.get(hypothesis.size() - 1).split("\\|\\-");
            hypothesis.set(hypothesis.size() - 1, split[0]);
            String expr;
            while((expr = reader.readLine()) != null) {
                expr = expr.replaceAll("(\\s)|(\\t)", "");
                if(expr.isEmpty())
                    continue;
                expressions.add(expr);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
            UtilChat.log("Unexpected error whilst trying to read input file & register output-file writer!");
        }finally {
            try {
                reader.close();
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void parseLexems() {
        lexemeAxioms = Axioms.HW1.stream().map(parser::parse).collect(Collectors.toList());
        lexemeHypothesis = hypothesis.stream().map(parser::parse).collect(Collectors.toList());
    }
    
    private void prove() {
        try {
            for(int i = 1; i <= expressions.size(); ++i) {
                String expr = expressions.get(i - 1);
                Lexeme parsed = parser.parse(expr);
                int prove = proveByAxioms(parsed);
                if(prove > 0) {
                    markAsProcessed(i, parsed);
                    UtilChat.log(pw, "(%d) %s (Сх. акс. %d)", i, expr, prove);
                    continue;
                }
                prove = proveByHypothesis(parsed);
                if(prove > 0) {
                    markAsProcessed(i, parsed);
                    UtilChat.log(pw, "(%d) %s (Предп. %d)", i, expr, prove);
                    continue;
                }
                //Modus Ponens section
                if(implications.containsKey(parsed)) {
                    Set<Integer> processed = implications.get(parsed);
                    boolean hasBeenProved = false;
                    for(int p : processed)
                        if(lexemesByIds.containsValue(lexemesByIds.get(p).getPrevious())) {
                            markAsProcessed(i, parsed);
                            UtilChat.log(pw, "(%d) %s (M.P. %d, %d)", i, expr, idsByLexemes.get(lexemesByIds.get(p).getPrevious()), p);
                            hasBeenProved = true;
                            break;
                        }
                    if(hasBeenProved)
                        continue;
                }
                markAsProcessed(i, parsed);
                UtilChat.log(pw, "(%d) %s (Не доказано)", i, expr);
            }
        }finally {
            pw.close();
        }
    }
    
    private void markAsProcessed(int id, Lexeme lexeme) {
        lexemesByIds.put(id, lexeme);
        idsByLexemes.put(lexeme, id);
        if(lexeme.getType() != LexemeType.IMPLICATION)
            return;
        Set<Integer> processed = implications.get(lexeme.getNext());
        if(processed == null) {
            processed = new HashSet<>();
            implications.put(lexeme.getNext(), processed);
        }
        processed.add(id);
    }
    
    private int proveLexeme(Lexeme lexeme, LocalChecker checker, List<Lexeme> hypothesis) {
        for(int i = 1; i <= hypothesis.size(); ++i)
            if(checker.check(hypothesis.get(i - 1), lexeme))
                return i;
        return 0;
    }
    
    private int proveByAxioms(Lexeme lexeme) {
        return proveLexeme(lexeme, new AxiomsChecker(), lexemeAxioms);
    }
    
    private int proveByHypothesis(Lexeme lexeme) {
        return proveLexeme(lexeme, new HypothesisChecker(), lexemeHypothesis);
    }
    
}
