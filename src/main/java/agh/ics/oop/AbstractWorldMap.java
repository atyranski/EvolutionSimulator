package agh.ics.oop;

import java.util.ArrayList;

abstract class AbstractWorldMap implements IWorldMap{
    protected ArrayList<Animal> animals = new ArrayList<>();
    protected ArrayList<Grass> grasses = new ArrayList<>();

    @Override
    public boolean canMoveTo(Vector2D position) {
        return false;
    }

    @Override
    public boolean place(Animal animal) {
        return false;
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return false;
    }

    @Override
    public Object objectAt(Vector2D position) {
        return null;
    }
}
