package agh.ics.oop;

import java.util.Arrays;

import static java.lang.System.out;

public class Animal implements IMapElement {
//    Attributes
    private int id;
    private int energy;
    private int orientation;
    private int maxEnergy;
    private int moveEnergyCost;
    private Vector2D position;
    private int[] genes = new int[32];

    private int age = 0;

    private AbstractWorldMap map;

//    Constructor
    public Animal(int id, int initialEnergy, int moveEnergyCost, Vector2D position, AbstractWorldMap map){
        this.id = id;
        this.energy = initialEnergy;
        this.orientation = randomFromRange(0, 7);
        this.maxEnergy = initialEnergy;
        this.moveEnergyCost = moveEnergyCost;
        this.position = position;
        generateGenes();

        this.map = map;
    }

    public Animal(int id, int initialEnergy, int moveEnergyCost, Vector2D position, AbstractWorldMap map, int[] genes, int energy){
        this.id = id;
        this.energy = energy;
        this.orientation = randomFromRange(0, 7);
        this.maxEnergy = initialEnergy;
        this.moveEnergyCost = moveEnergyCost;
        this.position = position;
        this.genes = genes;

        this.map = map;
    }

//    Static methods
    public static int[] combineGenes(Animal first, Animal second){
        int[] genes = new int[32];
        int[] firstGenes = first.getGenes();
        int[] secondGenes = second.getGenes();

        boolean randomSide = Math.random() < 0.5;
        int contribution = first.getEnergy() / (first.getEnergy() + second.getEnergy());

        if(randomSide){
            for(int i=0; i<contribution; i++) genes[i] = firstGenes[i];
            for(int i=contribution; i<32; i++) genes[i] = secondGenes[i];
        } else {
            for(int i=0; i<contribution; i++) genes[i] = secondGenes[i];
            for(int i=contribution; i<32; i++) genes[i] = firstGenes[i];
        }

        return genes;
    }

    public static int combineEnergy(Animal first, Animal second){
        return (int) (first.getEnergy() * 0.25 + second.getEnergy() * 0.25);
    }

//    Private methods
    private void generateGenes(){
        for(int i=0; i<32; i++){
            genes[i] = randomFromRange(0, 7);
        }
        Arrays.sort(genes);
    }

    private int changeOrientation(){
        return this.genes[randomFromRange(0, 31)];
//        this.orientation = (this.orientation + newOrientation) % 8;
    }

    private int randomFromRange(int min, int max){
        int result = min + (int)(Math.random() * ((max - min) + 1));
        if(result > max) result = max;
        return result;
    }

//    Public methods
    public void move(){
        this.age += 1;
//        out.println(this.id);
        int newOrientation = changeOrientation();
        if(newOrientation != 0 && newOrientation != 4) {
            this.orientation = (this.orientation + newOrientation) % 8;
            return;
        }

        int isForward = newOrientation == 0 ? 1 : -1;
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

        if(isForward == -1) shift = shift.opposite();

        Vector2D oldPosition = this.position;
        Vector2D newPosition = this.position.add(shift);

        if(newPosition.follows(this.map.getCornerBottomRight()) && newPosition.precedes(this.map.getCornerTopLeft())) {
            this.position = newPosition;
            this.map.positionChanged(oldPosition, this.position, this.id);
        } else {
            if(!map.isBordered()){
                if(newPosition.x < 0) newPosition.x = map.getWidth()-1;
                else if(newPosition.x > map.getWidth()-1) newPosition.x = 0;

                if(newPosition.y < 0) newPosition.y = map.getHeight()-1;
                else if(newPosition.y > map.getHeight()-1) newPosition.y = 0;

                this.position = newPosition;
                this.map.positionChanged(oldPosition, this.position, this.id);
            }
        }
        this.energy -= this.moveEnergyCost;
        if(this.energy <= 0) map.addToRemove(this);
    }

    public void eat(int energy){
        this.energy = Math.min(this.energy + energy, this.maxEnergy);
    }

    public void reproductionLost(){
        this.energy = (int) (this.energy * 0.75);
    }

    public int getOrientation() {
        return orientation;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int[] getGenes() {
        return genes;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    //    Interface methods
    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

}
