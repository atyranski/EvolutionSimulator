package agh.ics.oop;

import java.util.ArrayList;

public class WorldMap extends AbstractWorldMap {
    private int width;
    private int height;
    private int plantEnergy;
    private float jungleRatio;
    private Vector2D cornerTopRight;
    private Vector2D cornerBottomLeft;

    private int jungleWidth;
    private int jungleHeight;

    public WorldMap(int width, int height, int plantEnergy, float jungleRatio){
        this.width = width;
        this.height = height;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.cornerTopRight = new Vector2D(width,height);
        this.cornerBottomLeft = new Vector2D(0,0);

        this.jungleWidth = (int) (width * this.jungleRatio);
        this.jungleHeight = (int) (height * this.jungleRatio);

        generateGrass();
    }

    private void generateGrass(){
        int offsetX = (int) ((this.width - this.jungleWidth) / 2);
        int offsetY = (int) ((this.height - this.jungleHeight) / 2);

        for(int i=0; i<this.jungleWidth; i++){
            for (int j=0; j<this.jungleHeight; j++){
                Grass grass = new Grass(new Vector2D(offsetX + i, offsetY + j), plantEnergy);
                grasses.add(grass);
            }
        }
    }

    public ArrayList<Grass> getGrasses(){
        return this.grasses;
    }
}