package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static java.lang.System.out;

public class App extends Application {
    final private int INITIAL_ENERGY = 40;
    final private int MAP_SIZE = 600;
    final private int LEFT_PANEL_SIZE = 200;
    final private int TITLE_BAR_HEIGHT = 46;


    private int width = 100;
    private int height = 100;
    private int startEnergy = 40;
    private int moveEnergyCost = 2;
    private int plantEnergy = 10;
    private float jungleRatio = 0.5f;
    private float mapRatio = width / height;

    private boolean isRunning = false;

    private WorldMap worldMap1;

    private GridPane gp_map1;
    private GridPane gp_map2;

    private SimulationEngine engine1;

    private Thread thread1;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage){
        Platform.runLater(() -> {
            primaryStage.setTitle("Evolution Simulator");
            primaryStage.getIcons().add(new Image("windowIcon.png"));
//            primaryStage.setResizable(false);

            Label l_settings = new Label("Settings");
            l_settings.setFont(Font.font("Verdana", 25));

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

            VBox vbox_width = new VBox(l_width, tf_width);
            VBox vbox_height = new VBox(l_height, tf_height);
            VBox vbox_startEnergy = new VBox(l_startEnergy, tf_startEnergy);
            VBox vbox_moveEnergyCost = new VBox(l_moveEnergyCost, tf_moveEnergyCost);
            VBox vbox_plantEnergy = new VBox(l_plantEnergy, tf_plantEnergy);
            VBox vbox_jungleRatio = new VBox(l_jungleRatio, tf_jungleRatio);

            vbox_width.setMargin(tf_width, new Insets(5, 0, 10, 0));
            vbox_height.setMargin(tf_height, new Insets(5, 0, 10, 0));
            vbox_startEnergy.setMargin(tf_startEnergy, new Insets(5, 0, 10, 0));
            vbox_moveEnergyCost.setMargin(tf_moveEnergyCost, new Insets(5, 0, 10, 0));
            vbox_plantEnergy.setMargin(tf_plantEnergy, new Insets(5, 0, 10, 0));
            vbox_jungleRatio.setMargin(tf_jungleRatio, new Insets(5, 0, 10, 0));

            Button b_openSimulator = new Button();
            b_openSimulator.setText("Open simulator");
            b_openSimulator.setFont(Font.font("Verdana", 15));
            b_openSimulator.setMinSize(360, 45);
            b_openSimulator.setOnAction(actionEvent ->  {
                width = Integer.parseInt(tf_width.getText());
                height = Integer.parseInt(tf_height.getText());
                startEnergy = Integer.parseInt(tf_startEnergy.getText());
                moveEnergyCost = Integer.parseInt(tf_moveEnergyCost.getText());
                plantEnergy = Integer.parseInt(tf_plantEnergy.getText());
                jungleRatio = Float.parseFloat(tf_jungleRatio.getText());
                mapRatio = width / height;

                this.worldMap1 = new WorldMap(this.width, this.height, this.plantEnergy, this.jungleRatio, false);

                // tymczaswowo - potem bedzie generator pozycji zwierząt
//                Vector2D[] positions = new Vector2D[]{ new Vector2D(1,1), new Vector2D(0,0), new Vector2D(width-1,height-1)};
                Vector2D[] positions = new Vector2D[]{ new Vector2D(0,0), new Vector2D(10,10), new Vector2D(width,height)};
                this.engine1 = new SimulationEngine(this.worldMap1, positions, INITIAL_ENERGY);
                this.engine1.addObserver(this);

                this.thread1 = new Thread(this.engine1);
                thread1.start();

                Platform.runLater(() -> {
                    openNewWindow();
                });
            });

            VBox container = new VBox(l_settings, vbox_width, vbox_height, vbox_startEnergy, vbox_moveEnergyCost, vbox_plantEnergy, vbox_jungleRatio, b_openSimulator);
            container.setPadding(new Insets(10, 20, 10, 20));
            container.setMargin(l_settings, new Insets(0, 0, 10, 0));
            container.setMargin(b_openSimulator, new Insets(10, 0, 10, 0));

            Scene scene = new Scene(container, 400, 470);

            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    @Override
    public void stop(){
        System.out.println("//Stage is closing//");
    }

    private void openNewWindow(){
        Stage newWindow = new Stage();
        newWindow.setTitle("Simulation");
        newWindow.getIcons().add(new Image("windowIcon.png"));
//        newWindow.setResizable(false);

        Button b_start = new Button();
        Button b_stop = new Button();

        b_start.setText("Start");
        b_start.setMinSize(100, 30);
        b_stop.setMaxSize(100, 30);
        b_start.setOnAction(actionEvent ->  {
            this.isRunning = true;
            out.println(this.isRunning);
            engine1.setIsRunning(this.isRunning);

            b_start.setDisable(true);
            b_stop.setDisable(false);
        });

        b_stop.setText("Stop");
        b_stop.setDisable(true);
        b_stop.setMinSize(100, 30);
        b_stop.setMaxSize(100, 30);
        b_stop.setOnAction(actionEvent ->  {
            this.isRunning = false;
            out.println(this.isRunning);
            engine1.setIsRunning(this.isRunning);

            b_start.setDisable(false);
            b_stop.setDisable(true);
        });

        HBox hbox_buttonsRow = new HBox(b_start, b_stop);

        VBox vbox_leftPanel = new VBox(hbox_buttonsRow);
        vbox_leftPanel.setMinSize(LEFT_PANEL_SIZE,MAP_SIZE);

        gp_map1 = new GridPane();
        gp_map1.setStyle("-fx-background-color: blue;");
        gp_map1.setMinSize(MAP_SIZE + TITLE_BAR_HEIGHT,MAP_SIZE + TITLE_BAR_HEIGHT);
        gp_map1.setMaxSize(MAP_SIZE + TITLE_BAR_HEIGHT,MAP_SIZE + TITLE_BAR_HEIGHT);
        generateBoundaries(gp_map1);
        generateObjects(gp_map1, worldMap1);

        gp_map2 = new GridPane();
        gp_map2.setStyle("-fx-background-color: #C2A86F;");
        generateBoundaries(gp_map2);

        HBox container = new HBox(vbox_leftPanel, gp_map1, gp_map2);
        container.setStyle("-fx-background-color: #333333;");
        container.setMargin(gp_map2, new Insets(0, 0, 0, 2));



//        tu coś nie gra z rozmiarami map - są za małe
        Scene newScene = new Scene(container, LEFT_PANEL_SIZE + 2 * MAP_SIZE, (MAP_SIZE + TITLE_BAR_HEIGHT) * mapRatio);
        newWindow.setScene(newScene);
        newWindow.show();
        newWindow.setOnCloseRequest(e -> {
            engine1.setIsOpened(false);
        });
    }

    private void generateObjects(GridPane gp, WorldMap wp){
        int cellSize = MAP_SIZE / Math.max(width, height);

        for(Vector2D key: wp.getObjects().keySet()){

            IMapElement object = null;

            for(IMapElement element: wp.getObjects().get(key)){
                if(object == null) object = element;
                else if (object.getClass().equals(Grass.class) && element.getClass().equals(Animal.class)) object = element;
                else if (object.getClass().equals(Animal.class) && element.getClass().equals(Animal.class)) {
                    if(((Animal) object).getEnergy() <= ((Animal) element).getEnergy()) object = element;
                }
            }

            Label label = new Label("");
            label.setMinSize(cellSize, cellSize);
            label.setMaxSize(cellSize, cellSize);
            if(object.getClass().equals(Grass.class)) label.setStyle("-fx-background-color: #4BA173;");
            else {
                Animal obj = (Animal) object;
                if(obj.getEnergy() > 30) label.setStyle("-fx-background-color: #A82008;");
                else if(obj.getEnergy() > 20) label.setStyle("-fx-background-color: #BD751E;");
                else if(obj.getEnergy() > 10) label.setStyle("-fx-background-color: #23618F;");
                else if(obj.getEnergy() > 0) label.setStyle("-fx-background-color: #8B66A8;");
            }

            gp.add(label, key.x + 1, key.y + 1);
        }

    }

    private void generateBoundaries(GridPane gp){
        int cellSize = MAP_SIZE / width;

        Label boundary;
        boundary = new Label("");
        boundary.setMinSize(0, 0);
        boundary.setMaxSize(0, 0);
        boundary.setStyle("-fx-background-color: red;");
        gp.add(boundary, 0, 0);

        int sum = 0;
        for (int i=1; i<width+1; i++){
            boundary = new Label("");
            sum += cellSize;
            boundary.setMinSize(cellSize, 0);
            boundary.setMaxSize(cellSize, 0);
            boundary.setStyle("-fx-background-color: red;");
            gp.add(boundary, i, 0);
        }

        for (int i=1; i<height+1; i++){
            boundary = new Label("");
            boundary.setMinSize(0, cellSize);
            boundary.setMaxSize(0, cellSize);
            boundary.setStyle("-fx-background-color: red;");
            gp.add(boundary, 0, i);
        }
    }

    public void update(){
        Platform.runLater(() -> {
            gp_map1.getChildren().clear();
            generateBoundaries(gp_map1);
            generateObjects(gp_map1, worldMap1);
        });
    }
}
