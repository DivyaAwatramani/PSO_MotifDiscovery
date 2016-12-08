package com.ufl.motif;

import com.ufl.motif.preprocessor.RNAFold;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class FileOperations {
    private BufferedReader reader;
    private String filename;

    public FileOperations(String filename) {
        this.filename = filename;
    }

    public void openFile() {
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    private List<String> readFile() {
        List<String> records = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    System.out.println("Starts with comment: " + filename);
                    continue;
                }
                records.add(line.trim());
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        return records;
    }

    public String getSequence() {
        this.openFile();
        StringBuilder toReturn = new StringBuilder();
        readFile().forEach(f -> toReturn.append(f));
//        System.out.println(toReturn.toString());
        this.closeFile();
        return toReturn.toString();
    }

    public String getProcessedSequence() {
        this.openFile();
        RNAFold r = new RNAFold(this.getSequence());
        String toReturn = r.process();
        this.closeFile();
        return toReturn;
    }

    public void closeFile() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }
}
