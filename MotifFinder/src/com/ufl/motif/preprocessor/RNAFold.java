package com.ufl.motif.preprocessor;

public class RNAFold {
    private String RNAFoldString;
    private StringBuilder processedString = new StringBuilder();

    public RNAFold(String str) {
        this.RNAFoldString = str;
    }

    public String process() {
        char prev = '\0';
        int ss = 0;
        int h5 = 0;
        int h3 = 0;
        for (char ch : RNAFoldString.toCharArray()) {
            if (prev != ch) {
                switch (prev) {
                    case '.':
                        processedString.append("ss(" + ss + ")");
                        ss = 0;
                        break;
                    case '(':
                        processedString.append("h5(" + h5 + ")");
                        h5 = 0;
                        break;
                    case ')':
                        processedString.append("h3(" + h3 + ")");
                        h3 = 0;
                        break;
                }
            }
            switch (ch) {
                case '.':
                    ss++;
                    break;
                case '(':
                    h5++;
                    break;
                case ')':
                    h3++;
                    break;
            }
        }
        return processedString.toString();
    }
}
