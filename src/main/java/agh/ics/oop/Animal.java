package agh.ics.oop;

import java.util.Arrays;

public class Animal implements IMapElement {
    int energy;
    int orientation;
    Vector2D position;
    int[] genes = new int[32];

//    Constructor
    public Animal(int initialEnergy, Vector2D position){
        this.energy = initialEnergy;
        this.orientation = (int) (Math.random() * 8);
        generateGenes();
        changeOrientation();
        this.position = position;
    }

//    Private methods
    private void generateGenes(){
        for(int i=0; i<32; i++){
            genes[i] = (int) (Math.random() * 8);
        }
        Arrays.sort(genes);
    }

    private void changeOrientation(){
        this.orientation = this.genes[(int) (Math.random() * 33)];
    }

//    Public methods
    public int getEnergy() {
        return energy;
    }

    public int getOrientation() {
        return orientation;
    }

    public int[] getGenes() {
        return genes;
    }

//    Interface methods
    @Override
    public Vector2D getPosition() {
        return this.position;
    }
}
