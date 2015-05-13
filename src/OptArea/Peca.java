/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OptArea;

import java.util.ArrayList;

/**
 *
 * @author Eugenio
 */
public class Peca {
    private int id;
    private int[][] forma;
    private int[][] forma90;
    private int[][] forma180;
    private int[][] forma270;
    private int altura;
    private int largura;

    public Peca(int id, int[][] forma, int altura, int largura) {
        this.id = id;
        this.forma = forma;
        this.forma90 = rotMatrix(this.forma);
        this.forma180 = rotMatrix(this.forma90);
        this.forma270 = rotMatrix(this.forma180);
        
        this.altura = altura;
        this.largura = largura;
        
        /*
        ArrayList<int[][]> formasTest = new ArrayList<>(3);
        formasTest.add(this.forma);
        formasTest.add(this.forma90);
        formasTest.add(this.forma180);
        formasTest.add(this.forma270);
        for (int[][] form : formasTest){
            StringBuilder str = new StringBuilder();
            for (int i=0; i<form.length; i++){
                for (int j=0; j<form[0].length; j++){
                    str.append(form[i][j]);
                }
                str.append("\n");
            }
            System.out.println(str);
        }
        */  
    }
    
    @Override
    public String toString() {
        return "\n" + id + "\t" + altura + "\t" + largura + "\t" + forma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[][] getForma(int rot){
        switch (rot){
            case 0:
                return forma;
            case 1:
                return forma90;
            case 2:
                return forma180;
            case 3:
                return forma270;
            default:
                return null;
        }
    }

/*
	this method rotates a matrix 90 degrees to the right
	the largura and altura are the rotated matrix values
*/
    private int[][] rotMatrix(int[][] original)
    {
        int largura = original.length;
        int altura = original[0].length;
        int[][] rotated = new int[altura][largura];
        for (int i = 0; i < altura; ++i) {
            for (int j = 0; j < largura; ++j) {
                rotated[i][j] = original[largura - j - 1][i];
            }
        }
        return rotated;
    }
    
    
}
