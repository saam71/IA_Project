package statistics;

import experiments.ExperimentEvent;
import ga.GAEvent;
import ga.GAListener;
import ga.GeneticAlgorithm;
import ga.Individual;
import ga.Problem;
import utils.Maths;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Formatter;

public class StatisticBestAverage<E extends Individual, P extends Problem<E>> implements GAListener  {
    
    private final double[] values;
    private int run;
    
    public StatisticBestAverage(int numRuns) {
        values = new double[numRuns];
    }

    @Override
    public void generationEnded(GAEvent e) {    
    }

    @Override
    public void runEnded(GAEvent e) {
        GeneticAlgorithm<E, P> ga = e.getSource();
        values[run++] = ga.getBestInRun().getFitness();
    }

    @Override
    public void experimentEnded(ExperimentEvent e) {

        double average = Maths.average(values);
        double sd = Maths.standardDeviation(values, average); //used to quantify the amount of variation or dispersion of a set of data values...

        utils.FileOperations.appendToTextFile("statistic_average_fitness.xls", e.getSource() + "\tAverage:" + average + "\tDeviation:" + sd + "\r\n");
    }    
}
