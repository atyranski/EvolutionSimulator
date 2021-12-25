package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable {
    private final int MOVE_DELAY = 200;
    private boolean isRunning = false;
    private boolean isOpened = true;
    private int id = 0;
    private int day = 0;

    private int initialEnergy;
    private int moveEnergyCost;
    private List<Animal> animals = new ArrayList<>();

    private AbstractWorldMap map;
    private App application;
    private int mapNumber;

    public SimulationEngine(AbstractWorldMap map, Vector2D[] initialPositions, int initialEnergy, int moveEnergyCost, int mapNumber){
        this.map = map;
        this.initialEnergy = initialEnergy;
        this.moveEnergyCost = moveEnergyCost;
        this.mapNumber = mapNumber;

        for(Vector2D position: initialPositions){
            Animal animal = new Animal(id, initialEnergy, moveEnergyCost, position, map);
            map.place(animal);
            animals.add(animal);
            this.id += 1;
        }
    }

    @Override
    public void run() {
        while (this.isOpened) {
            if (this.isRunning) {
                out.printf("----NEXT DAY #%d----\n", day);
                map.removeDeadAnimals();
                this.moveAnimals();
                map.feedAnimals();
                this.animalReproduction();
                map.generateNewPlants();

                out.printf("Animals: %d\n\n", animals.size());
                day += 1;
            }

            application.update(this.mapNumber);

            try {
                Thread.sleep(MOVE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void moveAnimals(){
        ArrayList<Animal> aliveAnimals = new ArrayList<>();
        for(Animal animal : animals){
            if(animal.getEnergy() > 0){
                aliveAnimals.add(animal);
                animal.move();
            }
        }
        animals = aliveAnimals;
    }

    private void animalReproduction(){
        ArrayList<AbstractWorldMap.CustomTuple> tuples = map.animalReproduction();
        for(AbstractWorldMap.CustomTuple tuple: tuples){
            Animal animal = new Animal(id,
                    initialEnergy,
                    moveEnergyCost,
                    tuple.getFirstParent().getPosition(),
                    map,
                    Animal.combineGenes(tuple.getFirstParent(), tuple.getSecondParent()),
                    Animal.combineEnergy(tuple.getFirstParent(), tuple.getSecondParent())
            );
            map.place(animal);
            animals.add(animal);
            id += 1;
        }
    }

    @Override
    public void addObserver(App application) {
        this.application = application;
    }

    public void setIsRunning(boolean state){
        this.isRunning = state;
    }

    public void setIsOpened(boolean state){
        this.isOpened = state;
    }
}
