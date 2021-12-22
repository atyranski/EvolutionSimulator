package agh.ics.oop;

import java.util.Arrays;

import static java.lang.System.out;

public class Animal implements IMapElement {
//    Attributes
    int energy;
    int orientation;
    Vector2D position;
    int[] genes = new int[32];

    private AbstractWorldMap map;

//    Constructor

    public Animal(int initialEnergy, Vector2D position, AbstractWorldMap map){
        this.energy = initialEnergy;
        this.orientation = randomFromRange(0, 7);
        generateGenes();
        this.position = position;
        this.map = map;
    }

//    Private methods
    private void generateGenes(){
        for(int i=0; i<32; i++){
            genes[i] = randomFromRange(0, 7);
        }
        Arrays.sort(genes);
    }

    private void changeOrientation(){
        int newOrientation = this.genes[randomFromRange(0, 31)];
        this.orientation = (this.orientation + newOrientation) % 8;
    }

    private int randomFromRange(int min, int max){
        int result = min + (int)(Math.random() * ((max - min) + 1));
        if(result > max) result = max;
        return result;
    }

//    Public methods
    public void move(){
        changeOrientation();
        Vector2D shift = switch (this.orientation){
            case 0 -> new Vector2D(0, -1);
            case 1 -> new Vector2D(1, -1);
            case 2 -> new Vector2D(1, 0);
            case 3 -> new Vector2D(1, 1);
            case 4 -> new Vector2D(0, 1);
            case 5 -> new Vector2D(-1, 1);
            case 6 -> new Vector2D(-1, 0);
            case 7 -> new Vector2D(-1, -1);
            default -> throw new IllegalStateException("Unexpected value: " + this.orientation);
        };

        Vector2D oldPosition = this.position;
        Vector2D newPosition = this.position.add(shift);

        if(newPosition.follows(this.map.getCornerBottomRight()) && newPosition.precedes(this.map.getCornerTopLeft())) {
            this.position = newPosition;
            this.map.positionChanged(oldPosition, this.position);
            out.println("a");
            out.println(this.position);
        } else {
            if(!map.isBordered()){
                if(newPosition.x < 0) newPosition.x = map.getWidth()-1;
                else if(newPosition.x > map.getWidth()-1) newPosition.x = 0;

                if(newPosition.y < 0) newPosition.y = map.getHeight()-1;
                else if(newPosition.y > map.getHeight()-1) newPosition.y = 0;

                this.position = newPosition;
                this.map.positionChanged(oldPosition, this.position);
                out.println("b");
                out.println(this.position);
            }
        }
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
