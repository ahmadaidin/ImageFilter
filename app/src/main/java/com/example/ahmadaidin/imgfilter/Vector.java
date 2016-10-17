package com.example.ahmadaidin.imgfilter;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class Vector{
    int x;
    int y;
    int direction;

    public Vector(int x, int y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void moveUp(){

    }

    public void moveDown(){

    }

    public void moveLeft(){

    }

    public void moveRight(){

    }

    public void moveUpLeft(){

    }

    public void moveUpRight(){

    }

    public void moveDownLeft(){

    }

    public void moveDownRight(){

    }

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }

    public void changeDirection(int direction){
        this.direction = direction;
    }






}
