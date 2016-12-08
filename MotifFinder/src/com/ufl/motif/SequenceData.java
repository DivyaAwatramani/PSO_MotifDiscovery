package com.ufl.motif;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SequenceData {
    private static List<String> sequenceList;
    private static String inputSeq;
    //private static ConcurrentHashMap<String, Double> pwm = new ConcurrentHashMap<String, Double>();
    private static Map<String, Double> pwm = Collections.synchronizedMap(new HashMap<String, Double>());

    public static List<String> getSequenceList() {
        return sequenceList;
    }

    public static void load(List<String> fileNamesList) {
        sequenceList = fileNamesList.stream()
                     .map(f -> new FileOperations(f))
                     .map(f -> f.getSequence())
                     .collect(Collectors.toList());
    }

    public static void populatePWM() {
        pwm.put("A", 0.0);
        pwm.put("T", 0.0);
        pwm.put("C", 0.0);
        pwm.put("G", 0.0);
        pwm.put("default", 0.0);
        sequenceList.stream().forEach(seq -> {
            System.out.println(seq);
            for (char n : seq.toCharArray()) {
                switch (n) {
                    case 'A': pwm.put("A", pwm.get("A") + 1); break;
                    case 'T': pwm.put("T", pwm.get("T") + 1); break;
                    case 'C': pwm.put("C", pwm.get("C") + 1); break;
                    case 'G': pwm.put("G", pwm.get("G") + 1); break;
                    default: pwm.put("default", pwm.get("default") + 1);
                }
            }
        });
        System.out.println(pwm);
        int totalLength = sequenceList.stream().map(s -> s.length()).mapToInt(i -> i).sum();
        pwm.put("A", pwm.get("A")/totalLength);
        pwm.put("T", pwm.get("T")/totalLength);
        pwm.put("C", pwm.get("C")/totalLength);
        pwm.put("G", pwm.get("G")/totalLength);
        System.out.println(pwm);
        System.out.println(pwm.get("A")+pwm.get("T")+pwm.get("C")+pwm.get("G"));
    }

    public static String getProbableMotif(int startPos, int length) {
        return inputSeq.substring(startPos, startPos + length);
    }

    public static void loadSeq(List<String> strings) {
        FileOperations f = new FileOperations(strings.get(0));
        f.openFile();
        inputSeq = f.getSequence();
        f.closeFile();
    }
}
