package ga.geneticOperators;

import ga.GeneticAlgorithm;
import ga.PosVectorIndividual;

public class PosMutation <I extends PosVectorIndividual> extends Mutation<I> {

    public PosMutation(double probability) {
        super(probability);
    }

    public void run(I ind) {
        int indSize = ind.getNumGenes();
        for (int g = 0; g < indSize; g+=3) { 
            if (GeneticAlgorithm.random.nextDouble() < probability) {
                ind.setGene(g+1, (ind.getGene(g+1)+ GeneticAlgorithm.random.nextInt(2) == 0 ? -1 : 1));
                ind.setGene(g, (ind.getGene(g)+ GeneticAlgorithm.random.nextInt(2) == 0 ? -1 : 1));
                if(ind.getGene(g+2) == 3){
                    ind.setGene(g+2, 0);
                }else{
                    ind.setGene(g+2, ind.getGene(g+2)+1);
                }
            }
            if (GeneticAlgorithm.random.nextDouble() < probability) {
                ind.setGene(g+1, GeneticAlgorithm.random.nextInt(ind.getLargura()));
            }
            if (GeneticAlgorithm.random.nextDouble() < probability) {
                ind.setGene(g+1, GeneticAlgorithm.random.nextInt(ind.getLargura()));
            }
            if (GeneticAlgorithm.random.nextDouble() < probability) {
                if(ind.getGene(g+2) == 3){
                    ind.setGene(g+2, 0);
                }else{
                    ind.setGene(g+2, ind.getGene(g+2)+1);
                }
            }
            if(GeneticAlgorithm.random.nextDouble() < probability){
                ind.setGene(g, (ind.getGene(g)+ GeneticAlgorithm.random.nextInt(2) == 0 ? -1 : 1));
            }
            if(GeneticAlgorithm.random.nextDouble() < probability){
                ind.setGene(g+1, (ind.getGene(g+1)+ GeneticAlgorithm.random.nextInt(2) == 0 ? -1 : 1));
            }
            if(GeneticAlgorithm.random.nextDouble() < probability){
                ind.setGene(g+2, (GeneticAlgorithm.random.nextInt(4)));
            }
        }
    }
    
    @Override
    public String toString(){
        return "Position mutation (" + probability + ")";
    }
}