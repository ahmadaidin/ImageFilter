package com.example.ahmadaidin.imgfilter.Skeletonize;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public abstract class Skeletonizer {
    protected ArrayList<int[]> grayscale;
    public Skeletonizer(){

    }

    public Skeletonizer(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }
    public ArrayList<int[]> getGrayscale(){
        return this.grayscale;
    }

    public void setGrayscale(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }

    public abstract void skeletonize();
}
