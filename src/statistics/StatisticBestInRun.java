package statistics;

import experiments.ExperimentEvent;
import ga.GAEvent;
import ga.GAListener;
import ga.GeneticAlgorithm;
import ga.Individual;
import ga.Problem;

public class StatisticBestInRun<I extends Individual, P extends Problem<I>> implements GAListener {

    private I bestInExperiment;

    public StatisticBestInRun() {
    }

    @Override
    public void generationEnded(GAEvent e) {
    }

    @Override
    public void runEnded(GAEvent e) {
        GeneticAlgorithm<I, P> ga = e.getSource();
        if (bestInExperiment == null || ga.getBestInRun().getFitness() > bestInExperiment.getFitness()) {
            bestInExperiment = (I) ga.getBestInRun().clone();
        }
    }

    @Override
    public void experimentEnded(ExperimentEvent e) {
        utils.FileOperations.appendToTextFile("statistic_best_per_experiment_fitness.xls", e.getSource() + "\tFitness:" + bestInExperiment.getFitness() + "\r\n");
        utils.FileOperations.appendToTextFile("statistic_best_per_experiment.txt", "\r\n\r\n" +"###############Experiment Begin##################\n" +e.getSource()+ "\r\n" + bestInExperiment.SecToString()+bestInExperiment.printTela()+"###############Experiment End##################");
    }
}
