package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class App extends Application {
    final private int MAP_SIZE = 500;
    final private int LEFT_PANEL_SIZE = 200;
    final private int LINECHART_HEIGHT = 400;
    final private int CHART_RESOLUTION = 51;

    private int width = 50;
    private int height = 50;
    private int startEnergy = 100;
    private int moveEnergyCost = 2;
    private int plantEnergy = 20;
    private float jungleRatio = 0.5f;
    private int startAnimalAmount = 50;
    private int cellSize = MAP_SIZE / width;

    private boolean isModeNormal1 = true;
    private boolean isModeNormal2 = true;
    private ArrayList<String> magicLogs = new ArrayList<>();
    private Text t_logs;

    private boolean isRunning = false;

    private WorldMap worldMap1;
    private WorldMap worldMap2;

    private GridPane gp_map1;
    private GridPane gp_map2;

    private SimulationEngine engine1;
    private SimulationEngine engine2;

    private Thread thread1;
    private Thread thread2;

    private LineChart<String, Number> lineChart1;
    private LineChart<String, Number> lineChart2;

    private String fileName;
    private File fileMap1;
    private File fileMap2;
    private FileOutputStream file1;
    private FileOutputStream file2;

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
                isModeNormal1 = rb_normalMode1.isSelected();
                isModeNormal2 = rb_normalMode2.isSelected();

                this.worldMap1 = new WorldMap(this.width, this.height, this.plantEnergy, this.jungleRatio, false, this.moveEnergyCost);
                this.worldMap2 = new WorldMap(this.width, this.height, this.plantEnergy, this.jungleRatio, true, this.moveEnergyCost);

                Vector2D[] positions1 = this.generateAnimalPositions();
                Vector2D[] positions2 = this.generateAnimalPositions();
                this.engine1 = new SimulationEngine(this.worldMap1, positions1, startEnergy, moveEnergyCost, 1, isModeNormal1);
                this.engine1.addObserver(this);

                this.engine2 = new SimulationEngine(this.worldMap2, positions2, startEnergy, moveEnergyCost, 2, isModeNormal2);
                this.engine2.addObserver(this);

                this.thread1 = new Thread(this.engine1);
                this.thread2 = new Thread(this.engine2);
                thread1.start();
                thread2.start();

                Platform.runLater(() -> {
                    try {
                        openNewWindow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private void openNewWindow() throws IOException {
        Stage newWindow = new Stage();
        newWindow.setTitle("Simulation");
        newWindow.getIcons().add(new Image("windowIcon.png"));
//        newWindow.setResizable(false);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        fileName = "C:/PO/output/" + formatter.format(date);
        fileMap1 = new File(fileName + "MAP1.csv");
        fileMap2 = new File(fileName + "MAP2.csv");
        fileMap1.getParentFile().mkdirs();
        fileMap1.createNewFile();
        fileMap2.createNewFile();
        file1 = new FileOutputStream(fileMap1, false);
        file2 = new FileOutputStream(fileMap2, false);

        Button b_start1 = new Button();
        Button b_stop1 = new Button();
        Button b_start2 = new Button();
        Button b_stop2 = new Button();

        b_start1.setText("Start");
        b_start1.setMinSize(100, 30);
        b_stop1.setMaxSize(100, 30);
        b_start1.setOnAction(actionEvent ->  {
            this.isRunning = true;
//            out.println(this.isRunning);
            engine1.setIsRunning(this.isRunning);
//            engine2.setIsRunning(this.isRunning);

            b_start1.setDisable(true);
            b_stop1.setDisable(false);
        });

        b_start2.setText("Start");
        b_start2.setMinSize(100, 30);
        b_stop1.setMaxSize(100, 30);
        b_start2.setOnAction(actionEvent ->  {
            this.isRunning = true;
//            out.println(this.isRunning);
//            engine1.setIsRunning(this.isRunning);
            engine2.setIsRunning(this.isRunning);

            b_start2.setDisable(true);
            b_stop2.setDisable(false);
        });

        b_stop1.setText("Stop");
        b_stop1.setDisable(true);
        b_stop1.setMinSize(100, 30);
        b_stop1.setMaxSize(100, 30);
        b_stop1.setOnAction(actionEvent ->  {
            this.isRunning = false;
//            out.println(this.isRunning);
            engine1.setIsRunning(this.isRunning);
//            engine2.setIsRunning(this.isRunning);

            b_start1.setDisable(false);
            b_stop1.setDisable(true);
        });

        b_stop2.setText("Stop");
        b_stop2.setDisable(true);
        b_stop2.setMinSize(100, 30);
        b_stop2.setMaxSize(100, 30);
        b_stop2.setOnAction(actionEvent ->  {
            this.isRunning = false;
//            out.println(this.isRunning);
//            engine1.setIsRunning(this.isRunning);
            engine2.setIsRunning(this.isRunning);

            b_start2.setDisable(false);
            b_stop2.setDisable(true);
        });

        HBox hbox_buttonsRow1 = new HBox(b_start1, b_stop1);
        HBox hbox_buttonsRow2 = new HBox(b_start2, b_stop2);

        Label l_buttonsMap1 = new Label("Map 1: ");
        Label l_buttonsMap2 = new Label("Map 2: ");

        l_buttonsMap1.setTextFill(Color.color(1,1,1));
        l_buttonsMap2.setTextFill(Color.color(1,1,1));

        Label l_logs = new Label("Logs: ");
        t_logs = new Text();

        l_logs.setTextFill(Color.color(1,1,1));
        t_logs.setText("...");
        t_logs.setFill(Color.WHITE);

        VBox vbox_leftPanel = new VBox(l_buttonsMap1, hbox_buttonsRow1, l_buttonsMap2, hbox_buttonsRow2, l_logs, t_logs);
        vbox_leftPanel.setMinSize(LEFT_PANEL_SIZE,MAP_SIZE);
        vbox_leftPanel.setMargin(l_logs, new Insets(10, 10, 10, 10));
        vbox_leftPanel.setMargin(t_logs, new Insets(0, 10, 0, 10));

        cellSize = Math.round(MAP_SIZE / Math.max(width, height));
        int realMapWidth = cellSize * width;
        int realMapHeight = cellSize * height;

        gp_map1 = new GridPane();
        gp_map1.setStyle("-fx-background-color: #C2A86F;");
        gp_map1.setMinSize(realMapWidth + cellSize,realMapHeight + cellSize);
        gp_map1.setMaxSize(realMapWidth + cellSize,realMapHeight + cellSize);
        generateBoundaries(gp_map1);
        generateObjects(gp_map1, worldMap1);

        gp_map2 = new GridPane();
        gp_map2.setStyle("-fx-background-color: #C2A86F;");
        gp_map2.setMinSize(realMapWidth + cellSize,realMapHeight + cellSize);
        gp_map2.setMaxSize(realMapWidth + cellSize,realMapHeight + cellSize);
        generateBoundaries(gp_map2);
        generateObjects(gp_map2, worldMap2);

//        Chart map 1
        CategoryAxis xAxis1 = new CategoryAxis();
        NumberAxis yAxis1 = new NumberAxis();
        xAxis1.setAnimated(false);
        yAxis1.setAnimated(false);

        lineChart1 = new LineChart<>(xAxis1, yAxis1);
        lineChart1.setAnimated(false);
        lineChart1.setCreateSymbols(false); //hide dots

        XYChart.Series seriesAnimal1 = new XYChart.Series();
        XYChart.Series seriesGrass1 = new XYChart.Series();
        XYChart.Series seriesAvgEnergy1 = new XYChart.Series();
        XYChart.Series seriesAvgLifespan1 = new XYChart.Series();
        XYChart.Series seriesAvgChildren1 = new XYChart.Series();

        seriesAnimal1.setName("Animals");
        seriesGrass1.setName("Grass");
        seriesAvgEnergy1.setName("Average Energy");
        seriesAvgLifespan1.setName("Average Lifespan");
        seriesAvgChildren1.setName("Averge Children");

        lineChart1.getData().addAll(seriesAnimal1, seriesGrass1, seriesAvgEnergy1, seriesAvgLifespan1, seriesAvgChildren1);

//        Chart map 2
        CategoryAxis xAxis2 = new CategoryAxis();
        NumberAxis yAxis2 = new NumberAxis();
        xAxis2.setAnimated(false);
        yAxis2.setAnimated(false);

        lineChart2 = new LineChart<>(xAxis2, yAxis2);
        lineChart2.setAnimated(false);
        lineChart2.setCreateSymbols(false);

        XYChart.Series seriesAnimal2 = new XYChart.Series();
        XYChart.Series seriesGrass2 = new XYChart.Series();
        XYChart.Series seriesAvgEnergy2 = new XYChart.Series();
        XYChart.Series seriesAvgLifespan2 = new XYChart.Series();
        XYChart.Series seriesAvgChildren2 = new XYChart.Series();

        seriesAnimal2.setName("Animals");
        seriesGrass2.setName("Grass");
        seriesAvgEnergy2.setName("Average Energy");
        seriesAvgLifespan2.setName("Average Lifespan");
        seriesAvgChildren2.setName("Averge Children");

        lineChart2.getData().addAll(seriesAnimal2, seriesGrass2, seriesAvgEnergy2, seriesAvgLifespan2, seriesAvgChildren2);
//

        VBox vbox_mapContent1 = new VBox(gp_map1, lineChart1);
        VBox vbox_mapContent2 = new VBox(gp_map2, lineChart2);

        HBox container = new HBox(vbox_leftPanel, vbox_mapContent1, vbox_mapContent2);
        container.setStyle("-fx-background-color: #333333;");
        container.setMargin(vbox_mapContent2, new Insets(0, 0, 0, 2));

        Scene newScene = new Scene(container, LEFT_PANEL_SIZE + 2 * (realMapWidth + cellSize), realMapHeight + cellSize + LINECHART_HEIGHT);
        newWindow.setScene(newScene);
        newWindow.show();
        newWindow.setOnCloseRequest(e -> {
            engine1.setIsOpened(false);
            engine2.setIsOpened(false);
        });
    }

    private void generateObjects(GridPane gp, WorldMap wp){
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

    private void generatePlot(int[] data,LineChart<String,Number> lineChart, int map) throws IOException {
//        0 - day
//        1 - animalAmount
//        2 - grassAmount
//        3 - avgEnergy
//        4 - avgLifespan
//        5 - avgChildren
//        out.printf("%d, %d, %d, %d, %d, %d\n", data[0], data[1], data[2], data[3], data[4], data[5]);

        XYChart.Series seriesAnimal = lineChart.getData().get(0);
        seriesAnimal.getData().add(new XYChart.Data<>(String.valueOf(data[0]), data[1]));

        XYChart.Series seriesGrass = lineChart.getData().get(1);
        seriesGrass.getData().add(new XYChart.Data<>(String.valueOf(data[0]), data[2]));

        XYChart.Series seriesAvgEnergy = lineChart.getData().get(2);
        seriesAvgEnergy.getData().add(new XYChart.Data<>(String.valueOf(data[0]), data[3]));

        XYChart.Series seriesAvgLifespan = lineChart.getData().get(3);
        seriesAvgLifespan.getData().add(new XYChart.Data<>(String.valueOf(data[0]), data[4]));

        XYChart.Series seriesAvgChildren = lineChart.getData().get(4);
        seriesAvgChildren.getData().add(new XYChart.Data<>(String.valueOf(data[0]), data[5]));

        if (seriesAnimal.getData().size() > CHART_RESOLUTION){
            seriesAnimal.getData().remove(0);
            seriesGrass.getData().remove(0);
            seriesAvgEnergy.getData().remove(0);
            seriesAvgLifespan.getData().remove(0);
            seriesAvgChildren.getData().remove(0);
        }

        String[] line = new String[]{
                        String.valueOf(data[0]),
                        String.valueOf(data[1]),
                        String.valueOf(data[2]),
                        String.valueOf(data[3]),
                        String.valueOf(data[4]),
                        String.valueOf(data[5])
                };

        if(map == 1) file1.write((convertToCSV(line) + "\n").getBytes());
        else file2.write((convertToCSV(line) + "\n").getBytes());
    }

    private Vector2D[] generateAnimalPositions(){
        Vector2D[] positions = new Vector2D[this.startAnimalAmount];
        ArrayList<Vector2D> possible = new ArrayList<>();

        ArrayList<Integer> list_x = new ArrayList<>();
        ArrayList<Integer> list_y = new ArrayList<>();
        int howMuchY = (int) Math.ceil((float) startAnimalAmount / (float) width);

        for (int i=0; i<width; i++) list_x.add(i);
        for (int i=0; i<height; i++) list_y.add(i);
        Collections.shuffle(list_x);
        Collections.shuffle(list_y);


        for(int i=0; i<width; i++){
            for(int j=0; j<howMuchY; j++){
                possible.add(new Vector2D(list_x.get(i), list_y.get(j)));
            }
            Collections.shuffle(list_y);
        }

        for(int i=0; i<startAnimalAmount; i++){
            positions[i] = possible.get(i);
        }

        return positions;
    }

    public void update(int mapNumber){
        if(mapNumber == 1){
            Platform.runLater(() -> {
                gp_map1.getChildren().clear();
                generateBoundaries(gp_map1);
                generateObjects(gp_map1, worldMap1);
                try {
                    this.generatePlot(worldMap1.getStatistics(), lineChart1, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> {
                gp_map2.getChildren().clear();
                generateBoundaries(gp_map2);
                generateObjects(gp_map2, worldMap2);
                try {
                    this.generatePlot(worldMap2.getStatistics(), lineChart2, 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void notifyMagicMode(int day, int map){
        magicLogs.add("MAP #" + map + "\nDay: " + day + " | magic rule executed\n");
        String communicate = "";
        for(String log : magicLogs){
            communicate += log + "\n";
        }
        t_logs.setText(communicate);
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
