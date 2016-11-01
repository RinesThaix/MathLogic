package ru.luvas.mathlogic.utils;

import java.io.PrintWriter;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class UtilChat {

    public static void log(String s) {
        System.out.println(s);
    }
    
    public static void log(String s, Object... args) {
        log(String.format(s, args));
    }
    
    public static void log(PrintWriter pw, String s) {
        pw.println(s);
    }
    
    public static void log(PrintWriter pw, String s, Object... args) {
        log(pw, String.format(s, args));
    }
    
}
