package com.example.ahmadaidin.imgfilter;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 25/10/2016.
 */

public class Line {
    private Point p1;
    private Point p2;

    //default constructor
    public Line(){
        p1= new Point(0,0);
        p2= new Point(0,0);
    }

    //constructor param
    public Line(Point p1, Point p2){
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
    }

    //setter
    public void setLine(Point p1, Point p2){
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
    }


    //getter
    public Point getP1(){
        return p1;
    }

    public Point getP2(){
        return p2;
    }
    private  int toPositive(int a){
        return Math.max(a,-1*a);
    }

    public ArrayList<Point> getAllPoints(){
        ArrayList<Point> table = new ArrayList<>();

        int dx = toPositive(p2.x-p1.x);
        int dy = toPositive(p2.y-p1.y);
        int sx;
        int sy;
        int err = (dx>dy ? dx : -dy)/2, e2;
        int x = p1.x;
        int y = p1.y;
        if(p1.x<p2.x) sx = 1; else sx=-1;
        if(p1.y<p2.y) sy = 1; else sy=-1;

        for(;;){
            Point p = new Point(x,y);
            table.add(p);
            if (p.x==p2.x && p.y==p2.y) break;
            e2 = err;
            if (e2 >-dx) { err -= dy; x += sx; }
            if (e2 < dy) { err += dx; y += sy; }
        }
        return table;
    }

    public boolean isEmpty(){
        return p1.equals(p2);
    }

    public boolean isEqual(Line L){
        if(isEmpty() || L.isEmpty()){
            return false;
        } else {
            return (p1.equals(L.p1) && p2.equals(L.p2));
        }
    }

}
