package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Label label;

    public GuiElementBox(IMapElement object, int cellSize){
        label = new Label();
        label.setMinSize(cellSize, cellSize);
        label.setMaxSize(cellSize, cellSize);
        label.setStyle("-fx-background-color: green;");

    }

    public Label getLabel() {
        return this.label;
    }
}
