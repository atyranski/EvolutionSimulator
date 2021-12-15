package agh.ics.oop;

import java.util.Arrays;

import static java.lang.System.out;

public class Animal implements IMapElement {
//    Attributes
    int energy;
    int orientation;
    Vector2D position;
    int[] genes = new int[32];

    private IWorldMap map;

//    Constructor
    public Animal(int initialEnergy, Vector2D position, IWorldMap map){
        this.energy = initialEnergy;
        this.orientation = randomFromRange(0, 7);
        generateGenes();
        this.position = position;
        this.map = map;
        out.println(this.genes);
    }

//    Private methods
    private void generateGenes(){
        for(int i=0; i<32; i++){
            genes[i] = randomFromRange(0, 7);
        }
        Arrays.sort(genes);
    }

    private void changeOrientation(){
        this.orientation = this.genes[randomFromRange(0, 31)];
    }

    private int randomFromRange(int min, int max){
        int result = min + (int)(Math.random() * ((max - min) + 1));
        if(result > max) result = max;
        return result;
    }

//    Public methods
    public void move(){
        changeOrientation();
        out.println(this.position);
        Vector2D shift = switch (this.orientation){
            case 0 -> new Vector2D(0, 1);
            case 1 -> new Vector2D(0, 1);
            case 2 -> new Vector2D(0, 1);
            case 3 -> new Vector2D(0, 1);
            case 4 -> new Vector2D(0, 1);
            case 5 -> new Vector2D(0, 1);
            case 6 -> new Vector2D(0, 1);
            case 7 -> new Vector2D(0, 1);
            default -> throw new IllegalStateException("Unexpected value: " + this.orientation);
        };
    }

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
