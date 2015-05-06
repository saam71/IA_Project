package ga.geneticOperators;

import ga.GeneticAlgorithm;
import ga.PosVectorIndividual;

public class PosMutation <I extends PosVectorIndividual> extends Mutation<I> {

    public PosMutation(double probability) {
        super(probability);
    }

    public void run(I ind) {
//        int indSize = ind.getNumGenes();
//        for (int g = 0; g < indSize; g++) {
//            if (GeneticAlgorithm.random.nextDouble() < probability) {
//                ind.setGene(g, (ind.getGene(g) == I.ONE) ? I.ZERO : I.ONE);
//            }
//        }
    }
    
    @Override
    public String toString(){
        return "Position mutation (" + probability + ")";
    }
}