package agh.ics.oop;

import agh.ics.oop.gui.App;

public interface IEngine {

    void run();

    void addObserver(App application);
}
