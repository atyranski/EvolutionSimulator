package agh.ics.oop;

public class Grass implements IMapElement{
    Vector2D position;
    int plantEnergy;

    public Grass(Vector2D position, int plantEnergy){
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    public void setPlantEnergy(int plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }
}
