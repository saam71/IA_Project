package OptArea;

import experiments.Experiment;
import experiments.ExperimentListener;
import experiments.ExperimentsFactory;
import ga.GAListener;
import ga.GeneticAlgorithm;
import ga.geneticOperators.*;
import ga.selectionMethods.RouletteWheel;
import ga.selectionMethods.SelectionMethod;
import ga.selectionMethods.Tournament;
import statistics.StatisticBestAverage;
import statistics.StatisticBestInRun;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class OptExperimentsFactory extends ExperimentsFactory {

    private int populationSize;
    private int maxGenerations;
    private SelectionMethod<OptAreaIndividual, OptArea> selection;
    private Recombination<OptAreaIndividual> recombination;
    private Mutation<OptAreaIndividual> mutation;
    private OptArea optArea;
    private Experiment<OptExperimentsFactory, OptArea> experiment;

    public OptExperimentsFactory(File configFile) throws IOException {
        super(configFile);
    }

    public Experiment buildExperiment() throws IOException {
        numRuns = Integer.parseInt(getParameterValue("Runs"));
        populationSize = Integer.parseInt(getParameterValue("Population size"));
        maxGenerations = Integer.parseInt(getParameterValue("Max generations"));

        //SELECTION
        if (getParameterValue("Selection").equals("tournament")) {
            int tournamentSize = Integer.parseInt(getParameterValue("Tournament size"));
            selection = new Tournament<OptAreaIndividual, OptArea>(populationSize, tournamentSize);
        } else if (getParameterValue("Selection").equals("roulette wheel")) {
            selection = new RouletteWheel<OptAreaIndividual, OptArea>(populationSize);
        }

        //RECOMBINATION
        double recombinationProbability = Double.parseDouble(getParameterValue("Recombination probability"));
        if (getParameterValue("Recombination").equals("one_cut")) {
            recombination = new RecombinationOneCut<OptAreaIndividual>(recombinationProbability);
        } else if (getParameterValue("Recombination").equals("two_cuts")) {
            recombination = new RecombinationTwoCuts<OptAreaIndividual>(recombinationProbability);
        } else if (getParameterValue("Recombination").equals("uniform")) {
            recombination = new RecombinationUniform<OptAreaIndividual>(recombinationProbability);
        }

        //MUTATION
        double mutationProbability = Double.parseDouble(getParameterValue("Mutation probability"));
        //if (getParameterValue("Mutation").equals("binary")) {
            mutation = new PosMutation<>(mutationProbability);
       // }

        //PPROBABILILTY OF 1S AND FITNESS TYPE
        double probabilityOf1s = Double.parseDouble(getParameterValue("Probability of 1s"));
        int fitnessType = Integer.parseInt(getParameterValue("Fitness type"));

        //PROBLEM 
        optArea = OptArea.buildOptArea(new File(getParameterValue("Problem file")));
        optArea.setProb1s(probabilityOf1s);
        optArea.setFitnessType(fitnessType);

        String textualRepresentation = buildTextualExperiment();

        experiment = new Experiment<OptExperimentsFactory, OptArea>(this, numRuns, optArea, textualRepresentation);

        statistics = new ArrayList<ExperimentListener>();
        for (String statisticName : statisticsNames) {
            ExperimentListener statistic = buildStatistic(statisticName);
            statistics.add(statistic);
            experiment.addExperimentListener(statistic);
        }

        return experiment;
    }

    public GeneticAlgorithm generateGAInstance(int seed) {
        GeneticAlgorithm<OptAreaIndividual, OptArea> gat;

        gat = new GeneticAlgorithm<OptAreaIndividual, OptArea>(
                populationSize,
                maxGenerations,
                selection,
                recombination,
                mutation,
                new Random(seed));

        for (ExperimentListener statistic : statistics) {
            gat.addGAListener((GAListener) statistic);
        }

        return gat;
    }

    private ExperimentListener buildStatistic(String statisticName) {
        if (statisticName.equals("BestIndividual")) {
            return new StatisticBestInRun();
        }
        if (statisticName.equals("BestAverage")) {
            return new StatisticBestAverage(numRuns);
        }        
        return null;
    }

    private String buildTextualExperiment() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sb.append("Date:"+df.format(new Date())+"\t");
        sb.append("File Name:"+getParameterValue("Problem file")+"\t");
        sb.append("Population size:" + populationSize + "\t");
        sb.append("Max generations:" + maxGenerations + "\t");
        sb.append("Selection:" + selection + "\t");
        sb.append("Recombination:" + recombination + "\t");
        sb.append("Mutation:" + mutation + "\t");
        sb.append("Prob1s:" + optArea.getProb1s() + "\t");
        sb.append("Fitness type:" + optArea.getFitnessType());
        return sb.toString();
    }
}
