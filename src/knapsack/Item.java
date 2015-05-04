package knapsack;

public class Item {

    public String name;
    public double weight;
    public double value;

    public Item(String name, double weight, double value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\n" + name + "\t" + weight + "\t" + value;
    }
}
