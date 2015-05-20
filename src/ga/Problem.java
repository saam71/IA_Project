package ga;

import OptArea.Peca;

public interface Problem<E extends Individual> {

    /*
     * This method returns a new Individual. It acts like a kind of factory of
     * Individuals. This is a problem specific operation.
     */
    E getNewIndividual();
    
    Peca getPeca(int index);
}
