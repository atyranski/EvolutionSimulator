package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.System.out;

public class App extends Application {
    final private int MAP_SIZE = 500;
    final private int LEFT_PANEL_SIZE = 200;

    private int width = 50;
    private int height = 50;
    private int startEnergy = 100;
    private int moveEnergyCost = 2;
    private int plantEnergy = 20;
    private float jungleRatio = 0.5f;
    private int startAnimalAmount = 50;
    private float mapRatio = width / height;
    private int cellSize = MAP_SIZE / width;

    private boolean isRunning = false;

    private WorldMap worldMap1;
    private WorldMap worldMap2;

    private GridPane gp_map1;
    private GridPane gp_map2;

    private SimulationEngine engine1;
    private SimulationEngine engine2;

    private Thread thread1;
    private Thread thread2;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage){
        Platform.runLater(() -> {
            primaryStage.setTitle("Evolution Simulator");
            primaryStage.getIcons().add(new Image("windowIcon.png"));
            primaryStage.setResizable(false);

            Label l_settings = new Label("Settings");
            l_settings.setFont(Font.font("Verdana", 25));

            Label l_width = new Label("Map width: ");
            Label l_height = new Label("Map height: ");
            Label l_startEnergy = new Label("Energy on start: ");
            Label l_moveEnergyCost = new Label("Move energy cost: ");
            Label l_plantEnergy = new Label("Energy from plant: ");
            Label l_jungleRatio = new Label("Jungle ratio: ");
            Label l_startAnimalAmount = new Label("Animals on start: ");
            Label l_evolutionMode_1 = new Label("Boundless map: ");
            Label l_evolutionMode_2 = new Label("Limited map: ");

            TextField tf_width = new TextField();
            TextField tf_height = new TextField();
            TextField tf_startEnergy = new TextField();
            TextField tf_moveEnergyCost = new TextField();
            TextField tf_plantEnergy = new TextField();
            TextField tf_jungleRatio = new TextField();
            TextField tf_startAnimalAmount = new TextField();

            tf_width.setText(Integer.toString(width));
            tf_height.setText(Integer.toString(height));
            tf_startEnergy.setText(Integer.toString(startEnergy));
            tf_moveEnergyCost.setText(Integer.toString(moveEnergyCost));
            tf_plantEnergy.setText(Integer.toString(plantEnergy));
            tf_jungleRatio.setText(Float.toString(jungleRatio));
            tf_startAnimalAmount.setText(Integer.toString(startAnimalAmount));

//            /Radio Buttons objects>
            RadioButton rb_normalMode1 = new RadioButton("Normal");
            RadioButton rb_magicMode1 = new RadioButton("Magic");
            RadioButton rb_normalMode2 = new RadioButton("Normal");
            RadioButton rb_magicMode2 = new RadioButton("Magic");

            rb_normalMode1.setSelected(true);
            rb_normalMode2.setSelected(true);

            ToggleGroup tg_group1 = new ToggleGroup();
            ToggleGroup tg_group2 = new ToggleGroup();

            rb_normalMode1.setToggleGroup(tg_group1);
            rb_magicMode1.setToggleGroup(tg_group1);
            rb_normalMode2.setToggleGroup(tg_group2);
            rb_magicMode2.setToggleGroup(tg_group2);

            HBox hbox_rb_1 = new HBox(rb_normalMode1, rb_magicMode1);
            HBox hbox_rb_2 = new HBox(rb_normalMode2, rb_magicMode2);

            VBox vbox_group_1 = new VBox(l_evolutionMode_1, hbox_rb_1);
            VBox vbox_group_2 = new VBox(l_evolutionMode_2, hbox_rb_2);

            HBox hbox_evolutionMode = new HBox(vbox_group_1, vbox_group_2);
            hbox_evolutionMode.setHgrow(vbox_group_1, Priority.ALWAYS);
            hbox_evolutionMode.setHgrow(vbox_group_2, Priority.ALWAYS);
//            <Radio Buttons objects

            VBox vbox_width = new VBox(l_width, tf_width);
            VBox vbox_height = new VBox(l_height, tf_height);
            VBox vbox_startEnergy = new VBox(l_startEnergy, tf_startEnergy);
            VBox vbox_moveEnergyCost = new VBox(l_moveEnergyCost, tf_moveEnergyCost);
            VBox vbox_plantEnergy = new VBox(l_plantEnergy, tf_plantEnergy);
            VBox vbox_jungleRatio = new VBox(l_jungleRatio, tf_jungleRatio);
            VBox vbox_startAnimalAmount = new VBox(l_startAnimalAmount, tf_startAnimalAmount);

            vbox_width.setMargin(tf_width, new Insets(5, 0, 10, 0));
            vbox_height.setMargin(tf_height, new Insets(5, 0, 10, 0));
            vbox_startEnergy.setMargin(tf_startEnergy, new Insets(5, 0, 10, 0));
            vbox_moveEnergyCost.setMargin(tf_moveEnergyCost, new Insets(5, 0, 10, 0));
            vbox_plantEnergy.setMargin(tf_plantEnergy, new Insets(5, 0, 10, 0));
            vbox_jungleRatio.setMargin(tf_jungleRatio, new Insets(5, 0, 10, 0));
            vbox_startAnimalAmount.setMargin(tf_startAnimalAmount, new Insets(5, 0, 10, 0));
            vbox_group_1.setMargin(l_evolutionMode_1, new Insets(5, 0, 5, 0));
            vbox_group_2.setMargin(l_evolutionMode_2, new Insets(5, 0, 5, 0));
            hbox_rb_1.setMargin(rb_normalMode1, new Insets(0, 10, 0, 0));
            hbox_rb_2.setMargin(rb_normalMode2, new Insets(0, 10, 0, 0));

            Button b_openSimulator = new Button();
            b_openSimulator.setText("Open simulator");
            b_openSimulator.setFont(Font.font("Verdana", 15));
            b_openSimulator.setMinSize(360, 45);
            b_openSimulator.setOnAction(actionEvent ->  {
//                Getting values of parameters from user input
                width = Integer.parseInt(tf_width.getText());
                height = Integer.parseInt(tf_height.getText());
                startEnergy = Integer.parseInt(tf_startEnergy.getText());
                moveEnergyCost = Integer.parseInt(tf_moveEnergyCost.getText());
                plantEnergy = Integer.parseInt(tf_plantEnergy.getText());
                jungleRatio = Float.parseFloat(tf_jungleRatio.getText());
                startAnimalAmount = Integer.parseInt(tf_startAnimalAmount.getText());
                mapRatio = width / height;

                this.worldMap1 = new WorldMap(this.width, this.height, this.plantEnergy, this.jungleRatio, false);
                this.worldMap2 = new WorldMap(this.width, this.height, this.plantEnergy, this.jungleRatio, true);

                Vector2D[] positions1 = this.generateAnimalPositions();
                Vector2D[] positions2 = this.generateAnimalPositions();
//                Vector2D[] positions = new Vector2D[]{ new Vector2D(1,1), new Vector2D(0,0), new Vector2D(width-1,height-1)};
//                Vector2D[] positions = new Vector2D[]{ new Vector2D(0,0), new Vector2D(3,3), new Vector2D(25,25), new Vector2D(width,height)};
                this.engine1 = new SimulationEngine(this.worldMap1, positions1, startEnergy, moveEnergyCost, 1);
                this.engine1.addObserver(this);

                this.engine2 = new SimulationEngine(this.worldMap2, positions2, startEnergy, moveEnergyCost, 2);
                this.engine2.addObserver(this);

                this.thread1 = new Thread(this.engine1);
                this.thread2 = new Thread(this.engine2);
                thread1.start();
                thread2.start();

                Platform.runLater(() -> {
                    openNewWindow();
                });
            });

            VBox container = new VBox(l_settings, vbox_width, vbox_height,
                    vbox_startEnergy, vbox_moveEnergyCost, vbox_plantEnergy,
                    vbox_jungleRatio, vbox_startAnimalAmount, hbox_evolutionMode,
                    b_openSimulator
            );
            container.setPadding(new Insets(10, 20, 10, 20));
            container.setMargin(l_settings, new Insets(0, 0, 10, 0));
            container.setMargin(b_openSimulator, new Insets(20, 0, 10, 0));

            Scene scene = new Scene(container, 400, 580);

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
//            out.println(this.isRunning);
            engine1.setIsRunning(this.isRunning);
            engine2.setIsRunning(this.isRunning);

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
            engine2.setIsRunning(this.isRunning);

            b_start.setDisable(false);
            b_stop.setDisable(true);
        });

        HBox hbox_buttonsRow = new HBox(b_start, b_stop);

        VBox vbox_leftPanel = new VBox(hbox_buttonsRow);
        vbox_leftPanel.setMinSize(LEFT_PANEL_SIZE,MAP_SIZE);

        gp_map1 = new GridPane();
        gp_map1.setStyle("-fx-background-color: #C2A86F;");
        gp_map1.setMinSize(MAP_SIZE + cellSize,MAP_SIZE + cellSize);
        gp_map1.setMaxSize(MAP_SIZE + cellSize,MAP_SIZE + cellSize);
        generateBoundaries(gp_map1);
        generateObjects(gp_map1, worldMap1);

        gp_map2 = new GridPane();
        gp_map2.setStyle("-fx-background-color: #C2A86F;");
        gp_map2.setMinSize(MAP_SIZE + cellSize,MAP_SIZE + cellSize);
        gp_map2.setMaxSize(MAP_SIZE + cellSize,MAP_SIZE + cellSize);
        generateBoundaries(gp_map2);
        generateObjects(gp_map2, worldMap2);

        HBox container = new HBox(vbox_leftPanel, gp_map1, gp_map2);
        container.setStyle("-fx-background-color: #333333;");
        container.setMargin(gp_map2, new Insets(0, 0, 0, 2));

        Scene newScene = new Scene(container, LEFT_PANEL_SIZE + 2 * (MAP_SIZE + cellSize), MAP_SIZE * mapRatio + cellSize);
        newWindow.setScene(newScene);
        newWindow.show();
        newWindow.setOnCloseRequest(e -> {
            engine1.setIsOpened(false);
            engine2.setIsOpened(false);
        });
    }

    private void generateObjects(GridPane gp, WorldMap wp){
        cellSize = MAP_SIZE / width;

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
                if(obj.getEnergy() > startEnergy * 0.75) label.setStyle("-fx-background-color: #A82008;");
                else if(obj.getEnergy() > startEnergy * 0.5) label.setStyle("-fx-background-color: #BD751E;");
                else if(obj.getEnergy() > startEnergy * 0.25) label.setStyle("-fx-background-color: #23618F;");
                else if(obj.getEnergy() > 0) label.setStyle("-fx-background-color: #8B66A8;");
            }

            gp.add(label, key.x + 1, key.y + 1);
        }

    }

    private void generateBoundaries(GridPane gp){
        cellSize = MAP_SIZE / width;

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

    private Vector2D[] generateAnimalPositions(){
        Vector2D[] positions = new Vector2D[this.startAnimalAmount];

        ArrayList<Integer> list_x = new ArrayList<>();
        ArrayList<Integer> list_y = new ArrayList<>();
        for (int i=0; i<width; i++) list_x.add(i);
        for (int i=0; i<height; i++) list_y.add(i);
        Collections.shuffle(list_x);
        Collections.shuffle(list_y);

        for(int i=0; i<startAnimalAmount; i++){
            positions[i] = new Vector2D(list_x.get(i), list_y.get(i));
        }

        return positions;
    }

    public void update(int mapNumber){
        if(mapNumber == 1){
            Platform.runLater(() -> {
                gp_map1.getChildren().clear();
                generateBoundaries(gp_map1);
                generateObjects(gp_map1, worldMap1);
            });
        } else {
            Platform.runLater(() -> {
                gp_map2.getChildren().clear();
                generateBoundaries(gp_map2);
                generateObjects(gp_map2, worldMap2);
            });
        }

    }
}
