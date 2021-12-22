package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable {
    private final int MOVE_DELAY = 500;
    private boolean isRunning = false;
    private boolean isOpened = true;

    private int INITIAL_ENERGY;
    private List<Animal> animals = new ArrayList<>();

    private AbstractWorldMap map;
    private App application;

    public SimulationEngine(AbstractWorldMap map, Vector2D[] initialPositions, int INITIAL_ENERGY){
        this.map = map;
        this.INITIAL_ENERGY = INITIAL_ENERGY;

        for(Vector2D position: initialPositions){
            Animal animal = new Animal(INITIAL_ENERGY, position, map);
            map.place(animal);
            animals.add(animal);
        }
    }

    @Override
    public void run() {
        while (this.isOpened) {
            if (this.isRunning) {
//                out.println("siemano");
                for(Animal animal: animals){
                    animal.move();
                }
            }

            application.update();

            try {
                Thread.sleep(MOVE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
