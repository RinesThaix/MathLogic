package ru.luvas.mathlogic;

import ru.luvas.mathlogic.checkers.GlobalChecker;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Launcher {

    public static void main(String[] args) {
        GlobalChecker checker = new GlobalChecker();
        checker.check("input.txt", "output.txt");
    }
    
}
