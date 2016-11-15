package com.example.ahmadaidin.imgfilter.FrequencyDomain;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 15/11/2016.
 */

public abstract class FrequencyTransformer {
    protected Bitmap bitmap;
    protected Bitmap oriImage;
    protected ArrayList<ArrayList<Integer>> result;
    protected final double pi = 3.14;
    protected final double imag = Math.sqrt(-1);

    public FrequencyTransformer(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriImage = Bitmap.createBitmap(bitmap);
        result = new ArrayList<>();
    }

    public FrequencyTransformer() {
        result = new ArrayList<>();
    }

    public void setBitmap (Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriImage = Bitmap.createBitmap(bitmap);
    }

    public void reset() {
        bitmap = Bitmap.createBitmap(oriImage);
        result.clear();
    }

    public abstract void transform();

    public ArrayList<ArrayList<Integer>> getResult() {
        return result;
    }

    public abstract void inversTransform();

}
