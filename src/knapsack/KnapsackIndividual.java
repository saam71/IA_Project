package knapsack;

import ga.BitVectorIndividual;

public class KnapsackIndividual extends BitVectorIndividual <Knapsack>{

    private double value;
    private double weight;
    
    public KnapsackIndividual(Knapsack problem, int size, double prob1s){
        super(problem, size, prob1s);
    }
    
    public KnapsackIndividual(KnapsackIndividual original) {
        super(original);
        this.weight = original.weight;
        this.value = original.value;
    }
    
    public double computeFitness() {
        value = weight = 0;
        for (int i = 0; i < genome.length; i++) {
            if (genome[i] == ONE) {
                value += problem.getItem(i).value;
                weight += problem.getItem(i).weight;
            }
        }

        switch (problem.getFitnessType()) {
            case Knapsack.SIMPLE_FITNESS:
                fitness = (weight > problem.getMaximumWeight()) ? 0 : value;
                break;
            case Knapsack.PENALTY_FITNESS: //cannot be used with roulette wheel because fitness value may become negative
                double penalty = 0;
                if (weight > problem.getMaximumWeight()) {
                    penalty = problem.getMaxVP() * (weight - problem.getMaximumWeight());
                }
                fitness = value - penalty;
        }

        return fitness;
    }    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nWeight: " + weight + " (limit: " + problem.getMaximumWeight() + ")");
        sb.append("\nValue: " + value);        
        sb.append("\nfitness: " + fitness);        
        sb.append("\nItems: ");
        for (int i = 0; i < genome.length; i++) {
            if (genome[i] == ONE) {
                sb.append(problem.getItem(i));
            }
        }
        return sb.toString();
    }
    
    @Override
    public KnapsackIndividual clone() {
        return new KnapsackIndividual(this);
    }    
}
