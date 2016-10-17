package com.example.ahmadaidin.imgfilter;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class EdgeIterator {
    Bitmap bitmap;

    public EdgeIterator(Bitmap bitmap){
        this.bitmap = Bitmap.createBitmap(bitmap);
    }

    public  EdgeIterator(){

    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ArrayList<Integer> findDirection(){
        ArrayList<Integer> direction = new ArrayList<>();


        return direction;
    }

    public static ArrayList<Integer> findDirection(Bitmap bitmap){
        ArrayList<Integer> direction = new ArrayList<>();


        return direction;
    }

}
