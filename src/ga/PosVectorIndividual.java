/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga;

/**
 *
 * @author Eugenio
 */
public abstract class PosVectorIndividual <P extends Problem> extends Individual<P, PosVectorIndividual>{
    public static final boolean ONE = true;
    public static final boolean ZERO = false;

    protected boolean[] genome;
    
    public PosVectorIndividual(P problem, int size, double prob1s) {
        super(problem);
        genome = new boolean[size];
//        for (int g = 0; g < genome.length; g++) {
//            genome[g] = (GeneticAlgorithm.random.nextDouble() < prob1s) ? ONE : ZERO;
//        }
    }
    
    public PosVectorIndividual(PosVectorIndividual<P> original) {
        super(original);
        this.genome = new boolean[original.genome.length];
        System.arraycopy(original.genome, 0, genome, 0, genome.length);
    }
    
    public int getNumGenes() {
        return genome.length;
    }
    
    public boolean getGene(int g) {
        return genome[g];
    }
    
    public void setGene(int g, boolean alel) {
        genome[g] = alel;
    }

    public void swapGenes(BitVectorIndividual other, int g) {
        boolean aux = genome[g];
        genome[g] = other.genome[g];
        other.genome[g] = aux;
    }
}
