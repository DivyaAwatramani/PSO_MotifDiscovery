package com.ufl.motif.scores;

import com.ufl.motif.SequenceData;
import net.sourceforge.jswarm_pso.FitnessFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyFitnessFunction extends FitnessFunction {

    String motifSeq;
    public Map<String, Integer> map = new HashMap<>();

    /**
     * Fitness function to be used in Particle swarm optimization
     * @param motif
     * @return fitness score of the motif
     */
    private double fitness(String motif) {
        List<String> sequences = new ArrayList<>();
        double score = 0;
        for (String seq: sequences) {
            score += (getMotifOccurrence(motif, seq) + getLongestMotifRunInSequence(motif, seq)) / 1.0 * ts(seq);
        }
        return 0;
    }

    /**
     * refers to number of occurrences of motif m in all segments of all positions in sequence i.
     * @param motif
     * @param sequence
     * @return number of occurrences of motif m in all segments of all positions in sequence i.
     */
    private long getMotifOccurrence(String motif, String sequence) {
        return 0;
    }

    /**
     * refers to the length of the longest consecutive elements of the motif present in sequence i.
     * @param motif
     * @param sequence
     * @return the length of the longest consecutive elements of the motif present in sequence i.
     */
    private long getLongestMotifRunInSequence(String motif, String sequence) {
        return 0;
    }

    /**
     *
     * @param sequence
     * @return Total number of segments of all lengths and all indexes of sequence i.
     */
    private long ts(String sequence) {
        return 1;
    }

    @Override
    public double evaluate(double[] position) {
        //System.out.println((int)position[0] + " " + (int)position[1]);

        //System.out.println(position[0] + " " + position[1] + " " + position[2]);
        motifSeq = SequenceData.getProbableMotif((int)position[0], (int)position[1]);
//        System.out.println(motifSeq);

        int scores = SequenceData.getSequenceList()
                                 .stream()
                                 .parallel()
                                 .map(f -> match(f)).mapToInt(i -> i).sum();

        return scores;
    }

    private Integer match(String seq) {
        //System.out.println(seq);
        int matches = 0;
        for(int i = 0; i < seq.length() - motifSeq.length() - 1; ) {
            try {
                if (motifSeq.equals(seq.substring(i, i + motifSeq.length()))) {
                    ++matches;
                    i += motifSeq.length();
                }
            } catch (Exception e) {
                System.out.println(i + " " + motifSeq.length() + " " + seq.length());
            }
            ++i;
        }
        map.put(motifSeq, matches);
        //System.out.println(motifSeq + " " + matches);
        return matches;
    }
}
