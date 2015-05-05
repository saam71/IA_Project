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

    protected int[] genome;
    private int altura;
    private int largura;
    
    public PosVectorIndividual(P problem, int size, int altura, int largura, double prob1s) {
        super(problem);
        genome = new int[size];
        this.altura = altura;
        this.largura = largura;
//        for (int g = 0; g < genome.length; g++) {
//            genome[g] = (GeneticAlgorithm.random.nextDouble() < prob1s) ? ONE : ZERO;
//        }
    }
    
    public PosVectorIndividual(PosVectorIndividual<P> original) {
        super(original);
        this.genome = new int[original.genome.length];
        System.arraycopy(original.genome, 0, genome, 0, genome.length);
    }
    
    public int getNumGenes() {
        return genome.length;
    }
    
    public int getGene(int g) {
        return genome[g];
    }
    
    public void setGene(int g, int alel) {
        genome[g] = alel;
    }

    public void swapGenes(PosVectorIndividual other, int g) {
        int aux = genome[g];
        genome[g] = other.genome[g];
        other.genome[g] = aux;
    }
}
