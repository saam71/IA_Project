package ga.geneticOperators;

import ga.BitVectorIndividual;
import ga.GeneticAlgorithm;

public class BinaryMutation <I extends BitVectorIndividual> extends Mutation<I> {

    public BinaryMutation(double probability) {
        super(probability);
    }

    public void run(I ind) {
        int indSize = ind.getNumGenes();
        for (int g = 0; g < indSize; g++) {
            if (GeneticAlgorithm.random.nextDouble() < probability) {
                ind.setGene(g, (ind.getGene(g) == I.ONE) ? I.ZERO : I.ONE);
            }
        }
    }
    
    @Override
    public String toString(){
        return "Binary mutation (" + probability + ")";
    }
}