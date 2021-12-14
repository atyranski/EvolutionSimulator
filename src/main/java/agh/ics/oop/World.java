package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Application;

import static java.lang.System.exit;
import static java.lang.System.out;

public class World {

    public static void main(String[] args) {
        try {
            Application.launch(App.class, args);
        } catch(Exception ex) {
            out.println(ex);
            exit(0);
        }
    }
}
