package com.example.ahmadaidin.imgfilter.FrequencyDomain;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 15/11/2016.
 */

public class FourierTransformer extends FrequencyTransformer{

    public FourierTransformer(Bitmap bitmap) {
        super(bitmap);
    }

    public FourierTransformer() {
        super();
    }

    public void transform() {
        for(int v = 0; v<bitmap.getHeight(); v++) {
            ArrayList<Integer> row = new ArrayList();
            for (int u = 0; u<bitmap.getWidth();u++) {
                int elmt = 0;
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    for (int x = 0; x < bitmap.getWidth(); x++) {
                        elmt += bitmap.getPixel(x, y) * Math.exp(-imag * 2 * pi * (u*x/bitmap.getWidth()+v*y/bitmap.getHeight()));
                    }
                }
                elmt = elmt/(bitmap.getHeight()*bitmap.getWidth());
                row.add(elmt);
            }
            result.add(row);
        }
    }

    public void inversTransform() {
        for(int y = 0; y<bitmap.getHeight(); y++) {
            for (int x = 0; x<bitmap.getWidth();x++) {
                int elmt = 0;
                for (int v = 0; v < result.size(); v++) {
                    for (int u = 0; u < result.get(v).size(); u++) {
                        elmt += result.get(v).get(u) * Math.exp(-imag * 2 * pi * (u*x/bitmap.getWidth()+v*y/bitmap.getHeight()));
                    }
                }
                elmt = elmt/(bitmap.getHeight()*bitmap.getWidth());
                bitmap.setPixel(x,y,elmt);
            }
        }
    }


}
