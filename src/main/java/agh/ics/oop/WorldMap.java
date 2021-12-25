package agh.ics.oop;

import java.util.*;

import static java.lang.System.in;
import static java.lang.System.out;

public class WorldMap extends AbstractWorldMap {
//    Attributes
    private int width;
    private int height;
    private int plantEnergy;
    private float jungleRatio;
    private Vector2D cornerBottomRight;
    private Vector2D cornerTopLeft;

    int steppeGrassAmount = 0;
    int jungleGrassAmount = 0;

    private int offsetX;
    private int offsetY;
    private int shortSide;

    private int jungleWidth;
    private int jungleHeight;
    private int maxGrassInJungle;
    private int maxGrassInSteppe;

    private ArrayList<Vector2D> fieldsToFeed = new ArrayList<>();
    private ArrayList<Vector2D> fieldsToReproduce = new ArrayList<>();
    private ArrayList<Animal> recentDeadAnimal = new ArrayList<>();

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
        this.maxGrassInJungle = jungleWidth * jungleHeight;
        this.maxGrassInSteppe = (width * height) - maxGrassInJungle;

        this.offsetX = (this.width - this.jungleWidth) / 2;
        this.offsetY = (this.height - this.jungleHeight) / 2;
        this.shortSide = (int) ((width - (width * jungleRatio)) / 2);

        this.generateNewPlants();
    }

    @Override
    public void generateNewPlants(){
        if(steppeGrassAmount < maxGrassInSteppe) this.generateSteppeGrass();
        if(jungleGrassAmount < maxGrassInJungle) this.generateJungleGrass();
    }

    private void generateSteppeGrass(){
        Vector2D position;
        double quarter;

        while (true){
            quarter = Math.random();

            if(quarter < 0.25){
                // pierwsza
                position = new Vector2D(
                        (int) (0 + (Math.random() * ((width - offsetX) - 0))),
                        (int) (0 + (Math.random() * (offsetY - 0)))
                );
            } else if (quarter < 0.5){
                // druga
                position = new Vector2D(
                        (int) (0 + (Math.random() * (offsetX - 0))) + (width - offsetX),
                        (int) (0 + (Math.random() * ((width - offsetY) - 0)))
                );
            } else if (quarter < 0.75){
                // trzecia
                position = new Vector2D(
                        (int) (0 + (Math.random() * ((width - offsetX) - 0))) + shortSide,
                        (int) (0 + (Math.random() * (offsetY - 0))) + (width - shortSide)
                );
            } else {
                // czwarta
                position = new Vector2D(
                        (int) (0 + (Math.random() * (offsetX - 0))),
                        (int) (0 + (Math.random() * ((width - offsetY) - 0))) + shortSide
                );
            }

            if(!mapObjects.containsKey(position)) break;
        }


        Grass grass = new Grass(position, this.plantEnergy);
        mapObjects.put(position, new ArrayList<>(Arrays.asList(grass)));
        this.steppeGrassAmount += 1;
    }

    private void generateJungleGrass(){
        Vector2D position;

        while (true){
            int x = (int) (Math.random() * width*jungleRatio);

            int y = (int) (Math.random() * height*jungleRatio);

            position = new Vector2D(x + offsetX, y + offsetY);
            if(!mapObjects.containsKey(position)) break;
        }

        Grass grass = new Grass(position, this.plantEnergy);
        mapObjects.put(position, new ArrayList<>(Arrays.asList(grass)));
        this.jungleGrassAmount += 1;
    }

//    Public methods
    public HashMap<Vector2D, ArrayList<IMapElement>> getObjects(){
        return this.mapObjects;
    }

//    Inherited methods
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void feedAnimals() {
        super.feedAnimals();

        for(Vector2D field : this.fieldsToFeed){
            ArrayList<IMapElement> objects = mapObjects.get(field);
            if(objects.size() == 0 || objects.get(0).getClass().equals(Animal.class)) continue;
            Grass grass = (Grass) objects.get(0);

            if(objects.size() == 2){
                Animal animal = (Animal) objects.get(1);
                animal.eat(grass.getEnergy());

            } else {
                int topEnergy = 0;
                for(int i=1; i<objects.size(); i++){
                    Animal animal = (Animal) objects.get(i);
                    if(animal.getEnergy() >= topEnergy) topEnergy = animal.getEnergy();
                }

                ArrayList<Animal> strongest = new ArrayList<>();
                for(int i=1; i<objects.size(); i++){
                    Animal animal = (Animal) objects.get(i);
                    if(animal.getEnergy() == topEnergy) strongest.add(animal);
                }

                for(int i=0; i<strongest.size(); i++){
                    strongest.get(i).eat(grass.getEnergy()/strongest.size());
                }

            }
            objects.remove(0);
            if(new Vector2D(this.offsetX, this.offsetY).follows(field)
                    && new Vector2D(this.offsetX + this.jungleWidth, this.offsetY + this.offsetY).precedes(field)) this.jungleGrassAmount -= 1;
            else this.steppeGrassAmount -= 1;

        }
        this.fieldsToFeed = new ArrayList<>();
    }

    @Override
    public void addToRemove(Animal animal) {
        super.addToRemove(animal);
        this.recentDeadAnimal.add(animal);
    }

    @Override
    public void removeDeadAnimals() {
        super.removeDeadAnimals();

        for(Animal animal : recentDeadAnimal){
            if (animal.getEnergy() <= 0) {
                if(mapObjects.get(animal.getPosition()).size() == 1) mapObjects.remove(animal.getPosition());
                else{
                    ArrayList<IMapElement> objects = mapObjects.get(animal.getPosition());
                    int index = -1;
                    for(int i=0; i<objects.size(); i++){
                        if(objects.get(i).getClass().equals(Animal.class)){
                            Animal currAnimal = (Animal) objects.get(i);
                            if(currAnimal.getId() == animal.getId()) index = i;
                        }
                    }

                    if(index == -1) throw new IllegalArgumentException("Animal not found");

                    objects.remove(index);
                }

            }
        }
        recentDeadAnimal = new ArrayList<>();

    }

    @Override
    public ArrayList<CustomTuple> animalReproduction() {
        ArrayList<CustomTuple> tuples = super.animalReproduction();

        for(Vector2D field : this.fieldsToReproduce){

            ArrayList<IMapElement> objects = mapObjects.get(field);
            if(objects == null) continue;
            if(objects.size() == 0) continue;

            int index = objects.get(0).getClass().equals(Grass.class) ? 1 : 0;

            ArrayList<Animal> possible = new ArrayList<>();

            for(int i=index; i<objects.size(); i++){
                Animal animal = (Animal) objects.get(i);
                if(animal.getEnergy() >= animal.getMaxEnergy() * 0.5) possible.add(animal);
            }

            if(possible.size() < 2) continue;
            Collections.sort(possible, Comparator.comparingInt(Animal::getEnergy));

            CustomTuple tuple = new CustomTuple(possible.get(possible.size()-1), possible.get(possible.size()-2));
            possible.get(possible.size()-1).reproductionLost();
            possible.get(possible.size()-2).reproductionLost();
            tuples.add(tuple);
        }

        fieldsToReproduce = new ArrayList<>();
        return tuples;
    }

    @Override
    public void positionChanged(Vector2D oldPosition, Vector2D newPosition, int id) {
        ArrayList<IMapElement> objects = this.mapObjects.get(oldPosition);

        int index = -1;
        for(int i=0; i < objects.size(); i++){
            if (objects.get(i).getPosition().isEqual(newPosition) && objects.get(i).getClass().equals(Animal.class)) {
                Animal object = (Animal) objects.get(i);
                if(object.getId() == id) index = i;
            }
        }
        if(index == -1) throw new IllegalArgumentException("Object didn't find in arraylist");

        IMapElement animal = objects.get(index);
        objects.remove(index);

        if(objects.size() == 0) this.mapObjects.remove(oldPosition);
        else {
            ArrayList<IMapElement> toPut = new ArrayList<>(objects);
            this.mapObjects.put(oldPosition, toPut);
        }

        ArrayList<IMapElement> objectsInNew;
        if(this.mapObjects.get(newPosition) == null) objectsInNew = new ArrayList<>();
        else {
            objectsInNew = this.mapObjects.get(newPosition);
            if(objectsInNew.get(0).getClass().equals(Grass.class)) fieldsToFeed.add(newPosition);
            if(objectsInNew.size() >= 2 || objectsInNew.get(0).getClass().equals(Animal.class)) fieldsToReproduce.add(newPosition);
        }

        objectsInNew.add(animal);
        this.mapObjects.remove(newPosition);
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