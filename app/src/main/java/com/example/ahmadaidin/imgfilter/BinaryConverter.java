package com.example.ahmadaidin.imgfilter;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class BinaryConverter {
    private int threshold;

    public BinaryConverter(){
        this.threshold = 0;
    }

    public BinaryConverter(int threshold){
        this.threshold = threshold;
    }

    public void setThreshold(int threshold){
        this.threshold = threshold;
    }

    public int getThreshold(){
        return threshold;
    }

    public ArrayList<int[]> convertImage(ArrayList<int[]> grayscaleImg){
        ArrayList<int[]> result = new ArrayList<>(grayscaleImg);
        for(int i = 0; i<result.size();i++) {
            int[] newRow = new int[result.get(i).length];
            for(int j = 0; i<result.get(i).length;j++) {
                if(result.get(i)[j]<threshold) {
                    newRow[j] = 0;
                } else {
                    newRow[j] = 255;
                }
            }
            result.set(i,newRow);
        }
        return result;
    }
}
