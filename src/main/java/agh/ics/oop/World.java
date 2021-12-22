package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.Arrays;

import static java.lang.System.exit;
import static java.lang.System.out;

public class World {

    public static void main(String[] args) {

//        Animal animal = new Animal(new Vector2D(1,1), 0);
//        out.println("Before: ");
//        out.println(animal.getPosition());
//        out.println(animal.getOrientation());
//        out.println(Arrays.toString(animal.getGenes()));
//        animal.move();

//        Vector2D p1 = new Vector2D(-1,0);
//        out.println(p1.precedes(new Vector2D(0,0)));


        try {
            Application.launch(App.class, args);
        } catch(Exception ex) {
            out.println(ex);
            exit(0);
        }
    }
}
