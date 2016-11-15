package com.example.ahmadaidin.imgfilter.ObjectDetector;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 02/11/2016.
 */

public abstract class FaceDetector {
    protected Bitmap bitmap;
    protected Bitmap oriBitmap;

    ArrayList<ArrayList<Point>> faces;
    public FaceDetector(){
        faces = new ArrayList<>();
    }
    private ArrayList<Integer> skinColors;
    private ArrayList<ArrayList<Point>> skins;

    public FaceDetector(Bitmap bitmap){
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriBitmap = Bitmap.createBitmap(bitmap);
        faces = new ArrayList<>();
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }
    public abstract void detectFace();

    public int getFaceNum(){
        return faces.size();
    }

    public ArrayList<ArrayList<Point>> getFaces() {
        return faces;
    }

}
