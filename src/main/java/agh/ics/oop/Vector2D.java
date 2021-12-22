package agh.ics.oop;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2D {
    public int x;
    public int y;

    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean precedes(Vector2D other){
        if(other.x > this.x || other.y > this.y) return false;

        return true;
    }

    public boolean follows(Vector2D other){
        if(other.x < this.x || other.y < this.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2D upperRight(Vector2D other){
        return new Vector2D(max(other.x, this.x), max(other.y, this.y));
    }

    public Vector2D lowerLeft(Vector2D other) {
        return new Vector2D(min(other.x, this.x), min(other.y, this.y));
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(other.x + this.x, other.y + this.y);
    }

    public Vector2D substract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null) return false;
        if(getClass() != other.getClass()) return false;

        Vector2D check = (Vector2D) other;
//        if(check.hashCode() == other.hashCode()) return true;
//        return false;
        if(check.x != this.x || check.y != this.y) return false;
        return true;
    }

    public boolean isEqual(Vector2D other){
        if (this.x != other.x || this.y != other.y) return false;
        return true;
    }

    public Vector2D opposite(){
        return new Vector2D(-this.x, -this.y);
    }
}