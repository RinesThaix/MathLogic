package ru.luvas.mathlogic;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Axioms {

    public final static List<String> HW1 = Arrays.asList(new String[]{
            "A->B->A",
            "(A->B)->(A->B->C)->(A->C)",
            "A->B->A&B",
            "A&B->A",
            "A&B->B",
            "A->A|B",
            "B->A|B",
            "(A->C)->(B->C)->(A|B->C)",
            "(A->B)->(A->!B)->!A",
            "!!A->A"}
    );
    
}
