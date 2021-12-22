package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected boolean borderedMode;
    protected HashMap<Vector2D, ArrayList<IMapElement>> mapObjects = new HashMap<>();

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

    //MUSZE - BORDERY
    @Override
    public boolean canMoveTo(Vector2D position) {
        return true;
    }

//MUSZE
    @Override
    public boolean place(Animal animal) {
        if(mapObjects.get(animal.getPosition()) == null){
//            System.out.println("add new");
            mapObjects.put(animal.getPosition(), new ArrayList<>(Arrays.asList(animal)));
        } else {
//            System.out.println("add to existing");
            ArrayList<IMapElement> list = mapObjects.get(animal.getPosition());
            list.add(animal);
            mapObjects.remove(animal.getPosition());
            mapObjects.put(animal.getPosition(), list);
        }
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
