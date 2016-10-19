package com.example.ahmadaidin.imgfilter;

import android.graphics.Point;

import java.util.ArrayList;
/**
 * Created by Aidin - 2 on 19/10/2016.
 */

public class Feature {
    private ArrayList<Integer> direction;
    private ArrayList<Point> pixelPos;

    public Feature(){
        direction = new ArrayList<>();
        pixelPos = new ArrayList<>();
    }

    public Feature(ArrayList<Integer> direction, ArrayList<Point> pixelPos) {
        this.direction = direction;
        this.pixelPos = pixelPos;
    }

    public ArrayList<Integer> getDirection() {
        return direction;
    }

    public ArrayList<Point> getPixelPos() {
        return pixelPos;
    }

    public void addDirection(Integer elmt) {
        direction.add(elmt);
    }

    public void addPixelPos(Point elmt) {
        pixelPos.add(elmt);
    }

    public Integer getDirectionElmt(int idx) {
        return direction.get(idx);
    }

    public Point getPixelPosElmt(int idx) {
        return pixelPos.get(idx);
    }

    public void setDirectionElmt(int idx, Integer elmt) {
        direction.set(idx, elmt);
    }

    public void setPixelPosElmt(int idx, Point elmt) {
        pixelPos.set(idx, elmt);
    }

    public int directionSize(){
        return direction.size();
    }

    public int pixelPosSize() {
        return pixelPos.size();
    }

    public void clear() {
        direction.clear();
        pixelPos.clear();
    }
}
