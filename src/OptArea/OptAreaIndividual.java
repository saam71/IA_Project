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
    
    
    public OptAreaIndividual(OptArea problem, int size, int altura, int largura, double prob1s) {
        super(problem, size, altura, largura, prob1s);
        
//        System.out.println("genoma:");
//        for(int p = 0; p< genome.length; p+=3){
//            System.out.println("x:" + genome[p] + " y:" + genome[p+1] + " r:" + genome[p+2]);
//        }
//        System.out.println("\n");
    }

    public OptAreaIndividual(OptAreaIndividual original) {
        super(original);
    }
    
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
        return new OptAreaIndividual(this);
    }
    
    
    public String ToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("genoma: " + genome);
        sb.append("\n");
        return sb.toString();
    } 
}
