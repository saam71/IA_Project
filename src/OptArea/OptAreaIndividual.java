/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OptArea;

import ga.PosVectorIndividual;

/**
 *
 * @author Eugenio
 */
public class OptAreaIndividual extends PosVectorIndividual <OptArea>{

    private int id;
    private int[][] forma;
    private int altura;
    private int largura;
    
    public OptAreaIndividual(OptArea problem, int size, double prob1s) {
        super(problem, size, prob1s);
    }

//    public OptAreaIndividual(OptAreaIndividual original) {
//        super(original);
//        this.weight = original.weight;
//        this.value = original.value;
//    }
    
    @Override
    public double computeFitness() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void swapGenes(PosVectorIndividual other, int g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PosVectorIndividual clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
