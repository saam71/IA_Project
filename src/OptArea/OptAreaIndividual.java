/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OptArea;

import ga.PosVectorIndividual;
import java.util.Arrays;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author Eugenio
 */
public class OptAreaIndividual extends PosVectorIndividual <OptArea>{
    
    private int altura;
    private int largura;
    private int [][] tela;
    private int numPecas;
    private double penalty;
    
    public OptAreaIndividual(OptArea problem, int size, int altura, int largura, double prob1s) {
        super(problem, size, altura, largura, prob1s);
        
        this.altura = altura;
        this.largura = largura;
        this.tela = new int[altura][largura];
        this.numPecas = size;
        this.penalty = 0.5;
        /*
        System.out.println("genoma:");
        for(int p = 0; p< genome.length; p+=3){
            System.out.println("x:" + genome[p] + " y:" + genome[p+1] + " r:" + genome[p+2]);
        }
        System.out.println("\n");
        */
    }

    public OptAreaIndividual(OptAreaIndividual original) {
        super(original);
        this.altura = original.altura;
        this.largura = original.largura;
        this.tela = new int[altura][largura];
        this.numPecas = original.numPecas;
        this.penalty = original.penalty;
    }
    
    @Override
    public double computeFitness() {
        fitness = 0.0;
        int[] min = new int[2];
        min[0] = 0;
        min[1] = 0;
        int max[] = new int[2];
        max[0] = 0;
        max[1] = 0;
        int sobreposicao = 0;
        int outOfBounds = 0;
        int area = 0;
        int desperdicio = 0;
        
        //desenho da tela com as pecas e contagem das sobrposicoes e out of bounds
        for (int i = 0; i < numPecas; i ++){
            //Peca peca = problem.getPeca(i);
            int posX = getGene(i*3);
            int posY = getGene(i*3+1);
            int[][] forma =  problem.getPeca(i).getForma(getGene(i*3+2));
            
            for(int x=0; x < forma.length; x++){
                for(int y= 0; y < forma[0].length; y++){
                    if(posX+x > tela.length-1 || posY+y > tela[0].length-1 || posX+x< 0 || posY+y < 0){
                        outOfBounds+=1;
                    }else if(forma[x][y] != 0){
                        sobreposicao = (tela[posX+x][posY+y] != 0) ? sobreposicao+=1 : sobreposicao;
                        //tela[posX+x][posY+y]= forma[x][y];
                        tela[posX+x][posY+y]= problem.getPeca(i).getId();
                    }
                }
            }
        }
        
        //System.out.println(Arrays.deepToString(tela));
        
        //calcula o X min 
        for(int c=0; c < tela.length; c++){
            int somaX = 0;
            
            for(int y = 0; y<tela[0].length; y++){
                somaX += tela[c][y];
            }
            
            if(somaX!=0){
                min[0]= c;
                break;
            }
        }
        //calcula o Y min 
        for(int c=0; c < tela[0].length; c++){
            int somaY = 0;
            
            for(int x = 0; x<tela.length; x++){
                somaY += tela[x][c];
            }
            
            if(somaY!=0){
                min[1]= c;
                break;
            }
        }
        //calcula o X max 
        for(int c=tela.length-1; c > 0; c--){
            int somaX = 0;
            
            for(int y = tela[0].length-1; y>0; y--){
                somaX += tela[c][y];
            }
            
            if(somaX!=0){
                max[0]= c+1;
                break;
            }
        }
        //calcula o Y max
        for(int c=tela[0].length-1; c > 0; c--){
            int somaY = 0;
            
            for(int x = tela.length-1; x > 0; x--){
                somaY += tela[x][c];
            }
            
            if(somaY!=0){
                max[1]= c +1;
                break;
            }
        }
            
        //calculo da area de tela utilizada
        area = (max[0] - min[0])*(max[1] - min[1]);
        //System.out.println("minx=" + min[0] + " maxX=" + max[0] + " minY=" + min[1] + " maxY=" + max[1] );
        
        //calculo da area desperdicada
        for(int x = min[0]; x <max[0]; x++){
            for(int y =min[1]; y < max[1]; y++){
                if(tela[x][y] ==0){
                    desperdicio +=1;
                }
            }
        }
            
        int areaEfetiva = area - desperdicio;
        
        fitness = ((tela.length * tela[0].length)/areaEfetiva) - penalty*(sobreposicao+outOfBounds);
        
        return fitness;
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

    @Override
    public void printTela() {
        StringBuilder st = new StringBuilder();
        for (int x = 0 ; x < tela.length; x++ ){
            st.append("|");
            for (int y = 0 ; y < tela[0].length; y++){
               st.append(tela[x][y] + " ");
            }
            st.append("|\n");
        }
        System.out.println(st);
    }
    
}
