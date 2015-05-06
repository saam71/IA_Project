/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OptArea;

/**
 *
 * @author Eugenio
 */
public class Peca {
    private int id;
    private int[][] forma;
    private int altura;
    private int largura;

    public Peca(int id, int[][] forma, int altura, int largura) {
        this.id = id;
        this.forma = forma;
        this.altura = altura;
        this.largura = largura;
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
    
    
}
