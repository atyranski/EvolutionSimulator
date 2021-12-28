package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{

    class CustomTuple{
        Animal firstParent;
        Animal secondParent;

        public CustomTuple(Animal firstParent, Animal secondParent){
            this.firstParent = firstParent;
            this.secondParent = secondParent;
        }

        public Animal getFirstParent() {
            return firstParent;
        }

        public Animal getSecondParent() {
            return secondParent;
        }
    }

    protected boolean borderedMode;
    protected HashMap<Vector2D, ArrayList<IMapElement>> mapObjects = new HashMap<>();

    //    Statistics
    protected int animalAmount = 0;
    protected int topGenotype = 0;
    protected int sumEnergy = 0;
    protected int deadAnimalAmount = 0;
    protected int sumLifespan = 0;
    protected float avgChildrenAmount = 0;

    public boolean isBordered(){
        return this.borderedMode;
    }

    public Vector2D getCornerBottomRight() {
        return null;
    }

    public Vector2D getCornerTopLeft() {
        return null;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void feedAnimals(){}

    public void addToRemove(Animal animal){

    }

    public void removeDeadAnimals(int day){}

    public void generateNewPlants(){}

    public ArrayList<CustomTuple> animalReproduction(){
        return new ArrayList<>();
    }

    //MUSZE - BORDERY
    @Override
    public boolean canMoveTo(Vector2D position) {
        return true;
    }

//MUSZE
    @Override
    public boolean place(Animal animal) {
        if(mapObjects.get(animal.getPosition()) == null){
            mapObjects.put(animal.getPosition(), new ArrayList<>(Arrays.asList(animal)));
        } else {
            ArrayList<IMapElement> list = mapObjects.get(animal.getPosition());
            list.add(animal);
        }
        this.animalAmount += 1;
        this.sumEnergy += animal.getEnergy();
        return true;
    }

//NIE MUSZE
    @Override
    public boolean isOccupied(Vector2D position) {
        return false;
    }

//MOŻE
    @Override
    public Object objectAt(Vector2D position) {
        return null;
    }
}
