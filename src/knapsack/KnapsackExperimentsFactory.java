package knapsack;

import experiments.*;
import ga.GAListener;
import ga.GeneticAlgorithm;
import ga.geneticOperators.*;
import ga.selectionMethods.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import statistics.StatisticBestAverage;
import statistics.StatisticBestInRun;

public class KnapsackExperimentsFactory extends ExperimentsFactory {

    private int populationSize;
    private int maxGenerations;
    private SelectionMethod<KnapsackIndividual, Knapsack> selection;
    private Recombination<KnapsackIndividual> recombination;
    private Mutation<KnapsackIndividual> mutation;
    private Knapsack knapsack;
    private Experiment<KnapsackExperimentsFactory, Knapsack> experiment;

    public KnapsackExperimentsFactory(File configFile) throws IOException {
        super(configFile);
    }

    public Experiment buildExperiment() throws IOException {
        numRuns = Integer.parseInt(getParameterValue("Runs"));
        populationSize = Integer.parseInt(getParameterValue("Population size"));
        maxGenerations = Integer.parseInt(getParameterValue("Max generations"));

        //SELECTION
        if (getParameterValue("Selection").equals("tournament")) {
            int tournamentSize = Integer.parseInt(getParameterValue("Tournament size"));
            selection = new Tournament<KnapsackIndividual, Knapsack>(populationSize, tournamentSize);
        } else if (getParameterValue("Selection").equals("roulette wheel")) {
            selection = new RouletteWheel<KnapsackIndividual, Knapsack>(populationSize);
        }

        //RECOMBINATION
        double recombinationProbability = Double.parseDouble(getParameterValue("Recombination probability"));
        if (getParameterValue("Recombination").equals("one_cut")) {
            recombination = new RecombinationOneCut<KnapsackIndividual>(recombinationProbability);
        } else if (getParameterValue("Recombination").equals("two_cuts")) {
            recombination = new RecombinationTwoCuts<KnapsackIndividual>(recombinationProbability);
        } else if (getParameterValue("Recombination").equals("uniform")) {
            recombination = new RecombinationUniform<KnapsackIndividual>(recombinationProbability);
        }

        //MUTATION
        double mutationProbability = Double.parseDouble(getParameterValue("Mutation probability"));
        if (getParameterValue("Mutation").equals("binary")) {
            mutation = new BinaryMutation<KnapsackIndividual>(mutationProbability);
        }

        //PPROBABILILTY OF 1S AND FITNESS TYPE
        double probabilityOf1s = Double.parseDouble(getParameterValue("Probability of 1s"));
        int fitnessType = Integer.parseInt(getParameterValue("Fitness type"));

        //PROBLEM 
        knapsack = Knapsack.buildKnapsack(new File(getParameterValue("Problem file")));
        knapsack.setProb1s(probabilityOf1s);
        knapsack.setFitnessType(fitnessType);

        String textualRepresentation = buildTextualExperiment();

        experiment = new Experiment<KnapsackExperimentsFactory, Knapsack>(this, numRuns, knapsack, textualRepresentation);

        statistics = new ArrayList<ExperimentListener>();
        for (String statisticName : statisticsNames) {
            ExperimentListener statistic = buildStatistic(statisticName);
            statistics.add(statistic);
            experiment.addExperimentListener(statistic);
        }

        return experiment;
    }

    public GeneticAlgorithm generateGAInstance(int seed) {
        GeneticAlgorithm<KnapsackIndividual, Knapsack> ga;

        ga = new GeneticAlgorithm<KnapsackIndividual, Knapsack>(
                populationSize,
                maxGenerations,
                selection,
                recombination,
                mutation,
                new Random(seed));

        for (ExperimentListener statistic : statistics) {
            ga.addGAListener((GAListener) statistic);
        }

        return ga;
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
        sb.append("Population size:" + populationSize + "\t");
        sb.append("Max generations:" + maxGenerations + "\t");
        sb.append("Selection:" + selection + "\t");
        sb.append("Recombination:" + recombination + "\t");
        sb.append("Mutation:" + mutation + "\t");
        sb.append("Prob1s:" + knapsack.getProb1s() + "\t");
        sb.append("Fitness type:" + knapsack.getFitnessType());
        return sb.toString();
    }
}
