package com.ufl.motif;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.ufl.motif.scores.MyFitnessFunction;
import com.ufl.motif.scores.MyParticle;
import net.sourceforge.jswarm_pso.Swarm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.System.exit;

public class Main {
    @Parameter(names = {"--input", "-i"})
    String input;

    @Parameter(names = {"--reference", "-r"})
    String reference;

    public static void main(String... args) {
        if (args.length < 2) {
            System.out.println("Specify input directory for sequences");
            exit(1);
        }
        Main main = new Main();
        new JCommander(main, args);
        main.run();
    }

    public List<String> listFileNames(String directory) {
        List<String> files = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    System.out.println(filePath);
                    files.add(filePath.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            exit(2);
        }
        return files;
    }

    public void run() {

        long startTime = System.currentTimeMillis();

        SequenceData.load(listFileNames(input));
        SequenceData.loadSeq(listFileNames(reference));
        SequenceData.populatePWM();

        // Create a swarm (using 'MyParticle' as sample particle
        // and 'MyFitnessFunction' as finess function)
        MyFitnessFunction fitnessFunction = new MyFitnessFunction();
        Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES
                                       , new MyParticle()
                                       , fitnessFunction);
        // Set position (and velocity) constraints.
        // i.e.: where to look for solutions
        swarm.setMaxPosition(1);
        swarm.setMinPosition(0);
        swarm.setInertia(0.729844);
        swarm.setGlobalIncrement(1.49618);
        swarm.setParticleIncrement(1.49618);
        // length of seq, motif length
        double[] minPos = {0.0,   12.0, 4.0};
        double[] maxPos = {500.0, 12.0, 4.0};
        swarm.setMinPosition(minPos);
        swarm.setMaxPosition(maxPos);
        double[] vel = {50.0, 3.0, 0.0};
        swarm.setMaxVelocity(vel);
        // Optimize a few times
        for( int i = 0; i < 2000; i++ ) swarm.evolve();
        // Print en results
        System.out.println(entriesSortedByValues(fitnessFunction.map));
        System.out.println(swarm.toStringStats());
        System.out.println("Time taken " + (System.currentTimeMillis() - startTime));
    }

    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries,
                         new Comparator<Map.Entry<K,V>>() {
                             @Override
                             public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                                 return e2.getValue().compareTo(e1.getValue());
                             }
                         }
        );

        return sortedEntries;
    }
}
