package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2D;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

import static java.lang.System.out;

public class App extends Application {
    private static int INITIAL_ENERGY = 40;
    private static int MAP_SIZE = 550;
    private static int LEFT_PANEL_SIZE = 200;

    private int width = 100;
    private int height = 100;
    private int startEnergy = 40;
    private int moveEnergyCost = 2;
    private int plantEnergy = 10;
    private float jungleRatio = 0.5f;

    private boolean isRunning = false;

    @Override
    public void init() throws Exception {
        super.init();

//        Animal animal = new Animal(INITIAL_ENERGY, new Vector2D(1,1));
//        out.println(animal.getOrientation());
//        out.println(animal.getEnergy());
//        out.println(Arrays.toString(animal.getGenes()));

    }

    @Override
    public void start(Stage primaryStage){
        Platform.runLater(() -> {
            primaryStage.setTitle("Evolution Simulator");

            Label l_settings = new Label("Settings");

            TextField tf_width = new TextField();
            TextField tf_height = new TextField();
            TextField tf_startEnergy = new TextField();
            TextField tf_moveEnergyCost = new TextField();
            TextField tf_plantEnergy = new TextField();
            TextField tf_jungleRatio = new TextField();

            tf_width.setText(Integer.toString(width));
            tf_height.setText(Integer.toString(height));
            tf_startEnergy.setText(Integer.toString(startEnergy));
            tf_moveEnergyCost.setText(Integer.toString(moveEnergyCost));
            tf_plantEnergy.setText(Integer.toString(plantEnergy));
            tf_jungleRatio.setText(Float.toString(jungleRatio));

            Label l_width = new Label("Map width: ");
            Label l_height = new Label("Map height: ");
            Label l_startEnergy = new Label("Energy on start: ");
            Label l_moveEnergyCost = new Label("Move energy cost: ");
            Label l_plantEnergy = new Label("Energy from plant: ");
            Label l_jungleRatio = new Label("Jungle ratio: ");

            HBox hbox_width = new HBox(l_width, tf_width);
            HBox hbox_height = new HBox(l_height, tf_height);
            HBox hbox_startEnergy = new HBox(l_startEnergy, tf_startEnergy);
            HBox hbox_moveEnergyCost = new HBox(l_moveEnergyCost, tf_moveEnergyCost);
            HBox hbox_plantEnergy = new HBox(l_plantEnergy, tf_plantEnergy);
            HBox hbox_jungleRatio = new HBox(l_jungleRatio, tf_jungleRatio);

            Button b_openSimulator = new Button();
            b_openSimulator.setText("Open simulator");
            b_openSimulator.setMinSize(100, 30);
            b_openSimulator.setOnAction(actionEvent ->  {
                openNewWindow();
            });

            VBox container = new VBox(l_settings, hbox_width, hbox_height, hbox_startEnergy, hbox_moveEnergyCost, hbox_plantEnergy, hbox_jungleRatio, b_openSimulator);

            Scene scene = new Scene(container, 400, 450);

            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    private void openNewWindow(){
        Stage newWindow = new Stage();
        newWindow.setTitle("Simulation");

        Button b_start = new Button();
        b_start.setText("Start");
        b_start.setMinSize(100, 30);
        b_start.setOnAction(actionEvent ->  {
            this.isRunning = true;
            out.println(this.isRunning);
        });

        Button b_stop = new Button();
        b_stop.setText("Stop");
        b_stop.setMinSize(100, 30);
        b_stop.setOnAction(actionEvent ->  {
            this.isRunning = false;
            out.println(this.isRunning);
        });

        VBox vbox_leftPanel = new VBox(b_start, b_stop);
        vbox_leftPanel.setStyle("-fx-background-color: #333333;");
        vbox_leftPanel.setMinSize(LEFT_PANEL_SIZE,MAP_SIZE);

        GridPane gp_map1 = new GridPane();
        gp_map1.setStyle("-fx-background-color: #CCCCCC;");
        gp_map1.setMinSize(MAP_SIZE,MAP_SIZE);

        GridPane gp_map2 = new GridPane();
        gp_map1.setStyle("-fx-background-color: #999999;");
        gp_map2.setMinSize(MAP_SIZE,MAP_SIZE);

        HBox container = new HBox(vbox_leftPanel, gp_map1, gp_map2);

        newWindow.setScene(new Scene(container, LEFT_PANEL_SIZE + 2 * MAP_SIZE, MAP_SIZE));
        newWindow.show();
    }

    @Override
    public void stop(){
        System.out.println("//Stage is closing//");
    }
}
