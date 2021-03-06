package OptArea;

import ga.Problem;
import java.io.File;
import java.io.IOException;

public class OptArea implements Problem <OptAreaIndividual>{

    public static final int SIMPLE_FITNESS = 0;
    public static final int PENALTY_FITNESS = 1;    
    private Peca[] pecas;
    private int altura;
    private int largura;
    private double prob1s;
    private int fitnessType = SIMPLE_FITNESS;
    private double maxSobreOout;
    private double maxPerimeter;

    public OptArea(Peca[] pecas, int altura, int largura, double prob1s) {
        if (pecas == null) {
            throw new IllegalArgumentException();
        }
        this.pecas = new Peca[pecas.length];
        System.arraycopy(pecas, 0, this.pecas, 0, pecas.length);        
        this.prob1s = prob1s;
        this.altura = altura;
        this.largura = largura;
        maxSobreOout = computeMaxSobrOut();

        maxPerimeter = computeMaxPerimeter();
        System.out.println("largura " + largura + " altura " + altura );

    }
    
    @Override
    public OptAreaIndividual getNewIndividual(){
        return new OptAreaIndividual(this, pecas.length, altura, largura, prob1s);
    }

    public int getNumPecas() {
        return pecas.length;
    }   

    public Peca getPeca(int index) {
        return (index >= 0 && index < pecas.length) ? pecas[index] : null;
    }

    
    public double getProb1s(){
        return prob1s;
    }
    
    public void setProb1s(double prob1s){
        this.prob1s = prob1s;
    }
    
    public int getFitnessType(){
        return fitnessType;
    }
    
    public void setFitnessType(int fitnessType){
        this.fitnessType = fitnessType;
    }

    public double getMaxSobrOut() {
        return maxSobreOout;
    }
    public double getMaxPerimeter() {
        return maxPerimeter;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of pieces: ");
        sb.append(pecas.length);
        sb.append("\n");
        sb.append("Material size[HxW]: "+altura+"x"+largura);
        sb.append("\n\n#Pieces description:");
        for (Peca peca : pecas) {
            sb.append("\nid: "+peca.getId()+"\nDimensions[HxW]: "+peca.getForma(0).length+"x"+peca.getForma(0)[0].length+"\nForm:\n");
            for (int row = 0; row < peca.getForma(0).length; row++) {
                for (int column = 0; column < peca.getForma(0)[row].length; column++) {
                    sb.append(peca.getForma(0)[row][column]);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private double computeMaxSobrOut() {
        double soma=0;
        for (Peca peca : pecas) {
            soma += peca.getAltura() * peca.getLargura();
        }
        
        return soma;
    }
    
    private double computeMaxPerimeter() {
        double total=0;
        int[][] forma;
        for (Peca peca : pecas) {
            forma = peca.getForma(0);
            
            for(int x = 0; x <forma.length; x++){
                if(forma[x][0] != 0){
                    total += 1;
                }
        }
            for(int y =0; y < forma[0].length; y++){
                if(forma[0][y]!=0){
                    total +=1;
            }
        }
            
            for(int x = 0; x < forma.length; x++){
                for(int y= 0; y< forma[0].length; y++){
                    if(!(x+1>= forma.length) && forma[x][y] != forma[x+1][y]){
                        total++;
                    }
                    if(!(y +1 >=forma[0].length) && forma[x][y] != forma[x][y+1]){
                        total++;
                    }
                }
            }
        }
        
        
        return total;
    }

    public static OptArea buildOptArea(File file) throws IOException {
        java.util.Scanner f = new java.util.Scanner(file);
        int n = f.nextInt();
        int alturaTela = f.nextInt();
        int larguraTela = 0;
        Peca[] pecas = new Peca[n];

        for (int i = 0; i < pecas.length; i++) {
            int altura = f.nextInt();
            int largura = f.nextInt();
            int[][] forma = new int[altura][largura];
            
            if(altura>largura){
                larguraTela += altura;
            }
            else{
                larguraTela += largura;
            }
            
            for(int x = 0; x < forma.length; x++){
                for(int y = 0; y < forma[0].length; y++){
                    forma[x][y] = f.nextInt();
                }
            }
            pecas[i] = new Peca(i+1, forma, altura, largura);
        }

        return new OptArea(pecas, alturaTela, larguraTela/2, 0.5);
    }

    

    
}
