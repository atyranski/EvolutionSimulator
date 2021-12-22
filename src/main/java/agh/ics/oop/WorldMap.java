package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static java.lang.System.out;

public class WorldMap extends AbstractWorldMap {
//    Attributes
    private int width;
    private int height;
    private int plantEnergy;
    private float jungleRatio;
    private Vector2D cornerBottomRight;
    private Vector2D cornerTopLeft;

    private int jungleWidth;
    private int jungleHeight;

    private ArrayList<Vector2D> removedGrass = new ArrayList<>();
    private ArrayList<Vector2D> steppeGrass = new ArrayList<>();

//    Constructors
    public WorldMap(int width, int height, int plantEnergy, float jungleRatio, boolean borderedMode){
        this.width = width;
        this.height = height;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.borderedMode = borderedMode;
        this.cornerBottomRight = new Vector2D(width,height);
        this.cornerTopLeft = new Vector2D(0,0);

        this.jungleWidth = (int) (width * this.jungleRatio);
        this.jungleHeight = (int) (height * this.jungleRatio);

        generateGrass();
    }

//    Private methods
    private void generateGrass(){
        int offsetX = (int) ((this.width - this.jungleWidth) / 2);
        int offsetY = (int) ((this.height - this.jungleHeight) / 2);

        for(int i=0; i<this.jungleWidth; i++){
            for (int j=0; j<this.jungleHeight; j++){
                Vector2D position = new Vector2D(offsetX + i, offsetY + j);
                Grass grass = new Grass(position, plantEnergy);
                mapObjects.put(position, new ArrayList<>(Arrays.asList(grass)));
//                grasses.add(grass);
            }
        }

        multiplyGrass();
    }

    private void multiplyGrass(){
        if(!removedGrass.isEmpty()){
            Collections.shuffle(removedGrass);

            Vector2D position = removedGrass.get(0);
            Grass grass = new Grass(position, this.plantEnergy);
            mapObjects.put(position, new ArrayList<>(Arrays.asList(grass)));
//            grasses.add(new Grass(position, this.plantEnergy));

            removedGrass.remove(0);
        }

        Vector2D position;

        while (true){
            int steppeX = Math.random() < 0.5 ?
                    (int) (Math.random() * ((width - jungleWidth) / 2)) :
                    (int) ((Math.random() * (width - (width - ((width - jungleWidth) / 2)))) + (width - ((width - jungleWidth) / 2)));

            int steppeY = Math.random() < 0.5 ?
                    (int) (Math.random() * ((height - jungleHeight) / 2)) :
                    (int) ((Math.random() * (height - (height - ((height - jungleHeight) / 2)))) + (height - ((height - jungleHeight) / 2)));

            position = new Vector2D(steppeX, steppeY);
            if(!steppeGrass.contains(position)) break;
        }

        steppeGrass.add(position);
        Grass grass = new Grass(position, this.plantEnergy);
        mapObjects.put(position, new ArrayList<>(Arrays.asList(grass)));
//        grasses.add(new Grass(position, this.plantEnergy));
    }

//    Public methods
    public HashMap<Vector2D, ArrayList<IMapElement>> getObjects(){
        return this.mapObjects;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    //    Inherited methods
    @Override
    public void positionChanged(Vector2D oldPosition, Vector2D newPosition) {
        ArrayList<IMapElement> objects = this.mapObjects.get(oldPosition);

        int index = -1;
        for(int i=0; i < objects.size(); i++){
            if (objects.get(i).getPosition().isEqual(newPosition)) index = i;
        }
        if(index == -1) throw new IllegalArgumentException("Object didn't find in arraylist");

        IMapElement animal = objects.get(index);
        objects.remove(index);

        this.mapObjects.remove(oldPosition);
        this.mapObjects.put(oldPosition, objects);

        ArrayList<IMapElement> objectsInNew;
        if(this.mapObjects.get(newPosition) == null) objectsInNew = new ArrayList<>();
        else objectsInNew = this.mapObjects.get(newPosition);

        objectsInNew.add(animal);
        this.mapObjects.remove(oldPosition);
        this.mapObjects.put(newPosition, objectsInNew);

    }

    @Override
    public Vector2D getCornerBottomRight() {
        return cornerBottomRight;
    }

    @Override
    public Vector2D getCornerTopLeft() {
        return cornerTopLeft;
    }
}