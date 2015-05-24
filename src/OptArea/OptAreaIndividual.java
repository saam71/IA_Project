/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OptArea;

import ga.PosVectorIndividual;
import java.util.Arrays;

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
    private int area;
    private int desperdicio;
    private int sobreposicao;
    private int outOfBounds;
    private int energiaCorte;
    
    public OptAreaIndividual(OptArea problem, int size, int altura, int largura, double prob1s) {
        super(problem, size, altura, largura, prob1s);
        
        this.altura = altura;
        this.largura = largura;
        this.tela = new int[altura][largura];
        this.numPecas = size;
        this.penalty = 10;
        
        /*
        System.out.println("genoma:");
        for(int p = 0; p< genome.length; p+=3){
            System.out.println("x:" + genome[p] + " y:" + genome[p+1] + " r:" + genome[p+2]);
        }
        System.out.println("\n");
        */
    }

    public OptAreaIndividual(OptAreaIndividual original) {
        super(original, original.altura, original.largura);
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
        this.tela = new int [this.altura][this.largura];
        this.sobreposicao = 0;
        this.outOfBounds = 0;
        this.area = 0;
        this.desperdicio = 0;
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
                        //tela[posX+x][posY+y]= 1;
                        tela[posX+x][posY+y]= problem.getPeca(i).getId();
                    }
                }
            }
        }
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
        //calculo da area desperdicada e da energia de corte
        this.energiaCorte = 0;
        //inicio do calculo da energia só contabiliza a fronteira inicial
        for(int x = min[0]; x <max[0]; x++){
            if(tela[x][min[1]] != 0){
                energiaCorte += 1;
            }
        }
        for(int y =min[1]; y < max[1]; y++){
            if(tela[min[0]][y]!=0){
                energiaCorte +=1;
            }
        }
        //calculo de despedicio e da energia de corte restante
        for(int x = min[0]; x <max[0]; x++){
            for(int y =min[1]; y < max[1]; y++){
                if(tela[x][y] ==0){
                    desperdicio +=1;
                }
                //calculo de energia 
                if(!(x+1>= tela.length) && tela[x][y] != tela[x+1][y]){
                    energiaCorte++;
                }
                if(!(y +1 >=tela[0].length) && tela[x][y] != tela[x][y+1]){
                    energiaCorte++;
                }
            }
        }
        int areaEfetiva = area - desperdicio;

        /*########################################
        ##### AREA PARA CALCULO DE FITNESSES######
        ##########################################*/
        // FORMULA MUITO ARCAICA
        //fitness = ((tela.length * tela[0].length)/areaEfetiva) - 2*(sobreposicao+outOfBounds);
        //fitness = ((tela.length * tela[0].length)*2/areaEfetiva)-  penalty*(sobreposicao+outOfBounds);
        
        //fitness = 100+(((tela.length * tela[0].length)/area)- desperdicio - 5*(sobreposicao+outOfBounds));
                
//         if(sobreposicao > 3 || outOfBounds > 3){
//             fitness = (((tela.length * tela[0].length)/areaEfetiva) - desperdicio - energiaCorte - 3*penalty*(sobreposicao+outOfBounds));
//         }else{
//             fitness = (((tela.length * tela[0].length)/areaEfetiva) - desperdicio - energiaCorte - penalty*(sobreposicao+outOfBounds));
//         }
        
        
        //fitness = 2*(((tela.length * tela[0].length)/area)*2 - 2*desperdicio - energiaCorte - penalty*(sobreposicao+outOfBounds));
        
//        fitness = ((tela.length * tela[0].length)/areaEfetiva) + penalty*(problem.getMaxSobrOut()- (sobreposicao + outOfBounds)) - energiaCorte - desperdicio;
        if(areaEfetiva <=0 ){
            areaEfetiva =1;
        }
        fitness = (3*((tela.length * tela[0].length)/areaEfetiva)+ 2*(problem.getMaxPerimeter()-energiaCorte) 
                + this.penalty*(problem.getMaxSobrOut()- (sobreposicao + outOfBounds)))/100;

        //fitness = 100+(((tela.length * tela[0].length)/areaEfetiva) - 2 * desperdicio - energiaCorte - penalty*(sobreposicao+outOfBounds));
        /*########################################
        ##### AREA PARA CALCULO DE FITNESSES######
        ##########################################*/        
        return fitness;
    }

    @Override
    public PosVectorIndividual clone() {
        return new OptAreaIndividual(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fitness value: " + this.fitness + "\t\t Overlap: " + this.sobreposicao + "\n" +
                "Area: " + this.area + "\t\t Out Of Bounds: " + this.outOfBounds + "\n" +
                "Effective Area: " + (this.area - this.desperdicio) + "\n" +
                "Waste: " + this.desperdicio + "\n" +
                "Cut Energy: " + this.energiaCorte);
        //sb.append("genoma: " + genome);
        //sb.append("\n");
        return sb.toString();
    }


    public String SecToString(){
        computeFitness();
        StringBuilder sb = new StringBuilder();
        sb.append("Fitness value: " + this.fitness+"\n");
        sb.append("Area: " + this.area+"\n");
        sb.append("Effective Area: " + (this.area - this.desperdicio)+"\n");
        sb.append("Waste: " + this.desperdicio + "\n");
        sb.append("Cut Energy: " + this.energiaCorte+"\n");
        sb.append("Out Of Bounds: " + this.outOfBounds+"\n");
        sb.append("Overlap: " + this.sobreposicao+"\n");
        //sb.append("genoma: " + genome);
        //sb.append("\n");
        return sb.toString();
    }

    @Override
    public String printTela() {
        this.computeFitness();
        StringBuilder st = new StringBuilder();
        st.append("Best Solution:\n");
       /* st.append("STATS: \nfitness= ").append(getFitness()).append("\narea= ").
                append(this.area).append("\ndeperdicio= ").append(this.desperdicio).
                append("\nsobreposicoes= ").append(this.sobreposicao).append("\nout of bounds= ").
                append(this.outOfBounds).append("\n\n");*/
        for (int x = 0 ; x < tela.length; x++ ){
            st.append("|");
            for (int y = 0 ; y < tela[0].length; y++){
               st.append(tela[x][y] + " ");
            }
            st.append("|\n");
        }
        
        //st.append("\n" + Arrays.toString(genome));
        //System.out.println(st);
        return st.toString();
    }

    public int[][] getTela() {
        return this.tela;
    }
    
}
