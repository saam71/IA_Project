package ga.geneticOperators;

import ga.GeneticAlgorithm;
import ga.Individual;

public class RecombinationOneCut <I extends Individual> extends Recombination<I> {

    public RecombinationOneCut(double probability) {
        super(probability);
    }

    public void run(I ind1, I ind2) {
        int cut = GeneticAlgorithm.random.nextInt(ind1.getNumGenes());

        for (int g = 0; g < cut; g++) {
            ind1.swapGenes(ind2, g);
        }
    }
    
    @Override
    public String toString(){
        return "One cut recombination (" + probability + ")";
    }    
}