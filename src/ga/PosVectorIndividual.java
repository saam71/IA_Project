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

    protected int[] genome;
    private int altura;
    private int largura;
    
    public PosVectorIndividual(P problem, int size, int altura, int largura, double prob1s) {
        super(problem);
        genome = new int[size * 3];
        this.altura = altura;
        this.largura = largura;
        
        for(int g = 0; g < genome.length; g+=3){
            genome[g] = GeneticAlgorithm.random.nextInt(altura);
            genome[g+1]= GeneticAlgorithm.random.nextInt(largura);
            genome[g+2] = GeneticAlgorithm.random.nextInt(4);
        }
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
        int aux0 = genome[g];
        int aux1 = genome[g+1];
        int aux2 = genome[g+2];
        genome[g] = other.genome[g];
        genome[g+1] = other.genome[g+1];
        genome[g+1] = other.genome[g+1];

        other.genome[g] = aux0;
        other.genome[g+1] = aux1;
        other.genome[g+2] = aux2;

    }
    
}

