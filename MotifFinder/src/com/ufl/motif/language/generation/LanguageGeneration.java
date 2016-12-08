package com.ufl.motif.language.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class LanguageGeneration {

    private static Stack<Character> getStartingSeq() {
        Stack<Character> list = new Stack<>();
        list.add('Y');
        list.add('H');
        list.add('X');
        list.add('h');
        list.add('Y');
        return list;
    }

    public static List<String> generateLanguage(int n, int ss, int h5, int h3) {
        List<String> sequences = new ArrayList<>();
        int i = n;
        while (i-- > 0) {
            List<Character> seq = new ArrayList<>();
            Stack<Character> list = getStartingSeq();
            while(!list.isEmpty()) {
                if (isTerminal(list.peek())) {
                    seq.add(list.pop());
                } else {
                    expand(list);
                }
            }
            //validateSequence(seq, ss, h5, h3);
        }
        return sequences;
    }


    private static void expand(Stack<Character> list) {
        List<Character> ls = getSymbolSeq(list.pop());
        ls.stream().forEach(c -> list.push(c));

    }

    private static List<Character> getSymbolSeq(Character symbol) {
        // pattern is reversed as later it will be pushed on stack
        switch(symbol) {
            case 'M': return (Arrays.asList('h', 'X', 'H'));
            case 'X': return getRandom() > 0.5? Arrays.asList('S', 'Y', 'M', 'S') : Arrays.asList('S');
            case 'Y': return getRandom() > 0.5? Arrays.asList('S') : Arrays.asList();
        }
        return Arrays.asList();
    }

    private static double getRandom() {
        return Math.random();
    }

    private static boolean isTerminal(Character peek) {
        if (peek == 'H' || peek == 'h' || peek == 'S') {
            return true;
        }
        return false;
    }
}
