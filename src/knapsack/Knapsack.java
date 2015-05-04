package knapsack;

import ga.Problem;
import java.io.File;
import java.io.IOException;

public class Knapsack implements Problem <KnapsackIndividual>{

    public static final int SIMPLE_FITNESS = 0;
    public static final int PENALTY_FITNESS = 1;    
    private Item[] items;
    private double maximumWeight;
    private double prob1s;
    private int fitnessType = SIMPLE_FITNESS;
    private double maxVP;

    public Knapsack(Item[] items, double maximumWeight, double prob1s) {
        if (items == null) {
            throw new IllegalArgumentException();
        }
        this.items = new Item[items.length];
        System.arraycopy(items, 0, this.items, 0, items.length);        
        this.maximumWeight = maximumWeight;
        this.prob1s = prob1s;
        maxVP = computeMaxVP();
    }
    
    public KnapsackIndividual getNewIndividual(){
        return new KnapsackIndividual(this, items.length, prob1s);
    }

    public int getNumItems() {
        return items.length;
    }   

    public Item getItem(int index) {
        return (index >= 0 && index < items.length) ? items[index] : null;
    }

    public double getMaximumWeight() {
        return maximumWeight;
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

    public double getMaxVP() {
        return maxVP;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("# of itens: ");
        sb.append(items.length);
        sb.append("\n");        
        sb.append("Weight limit: ");
        sb.append(maximumWeight);
        sb.append("\n");
        sb.append("Items:");
        sb.append("\nId\tWeight\tValue");
        for (Item item : items) {
            sb.append(item);
        }
        return sb.toString();
    }

    private double computeMaxVP() {
        double max = items[0].value / items[0].weight;
        for (int i = 1; i < items.length; i++) {
            double divVP = items[i].value / items[i].weight;
            if (divVP > max) {
                max = divVP;
            }
        }
        return max;
    }

    public static Knapsack buildKnapsack(File file) throws IOException {
        java.util.Scanner f = new java.util.Scanner(file);

        int n = f.nextInt();
        int pM = f.nextInt();

        Item[] items = new Item[n];

        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(Integer.toString(i + 1), f.nextInt(), f.nextInt());
        }

        return new Knapsack(items, pM, 0.5);
    }
}
