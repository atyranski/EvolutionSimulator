package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

abstract class AbstractWorldMap implements IWorldMap{
//    protected ArrayList<Animal> animals = new ArrayList<>();
//    protected ArrayList<Grass> grasses = new ArrayList<>();

    protected HashMap<Vector2D, ArrayList<IMapElement>> mapObjects = new HashMap<>();

    @Override
    public boolean canMoveTo(Vector2D position) {
        return false;
    }

//TO MUSZE
    @Override
    public boolean place(Animal animal) {
        if(mapObjects.get(animal.getPosition()) == null){
            System.out.println("add new");
            mapObjects.put(animal.getPosition(), new ArrayList<>(Arrays.asList(animal)));
        } else {
            System.out.println("add to existing");
            ArrayList<IMapElement> list = mapObjects.get(animal.getPosition());
            list.add(animal);
            mapObjects.remove(animal.getPosition());
            mapObjects.put(animal.getPosition(), list);
        }
        return true;
    }

//TO MUSZE
    @Override
    public boolean isOccupied(Vector2D position) {
        return false;
    }

//TO MUSZE
    @Override
    public Object objectAt(Vector2D position) {
        return null;
    }
}
