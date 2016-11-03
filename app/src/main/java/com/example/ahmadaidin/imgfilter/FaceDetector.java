package com.example.ahmadaidin.imgfilter;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 02/11/2016.
 */

public abstract class FaceDetector {
    Bitmap bitmap;
    ArrayList<Bitmap> faces;
    public FaceDetector(){
        faces = new ArrayList<>();
    }

    public FaceDetector(Bitmap bitmap){
        this.bitmap = Bitmap.createBitmap(bitmap);
        faces = new ArrayList<>();
    }

    public abstract void detectFace();

    public int getFaceNum(){
        return faces.size();
    }
}
