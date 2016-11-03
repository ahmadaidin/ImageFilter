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
        ArrayList<int[]> result = new ArrayList<>();
        for(int i = 0; i<grayscaleImg.size();i++) {
            int[] newRow = new int[grayscaleImg.get(i).length];
            for(int j = 0; j<grayscaleImg.get(i).length;j++) {
                if(grayscaleImg.get(i)[j]<threshold) {
                    newRow[j] = 0;
                } else {
                    newRow[j] = 255;
                }
            }
            result.add(newRow);
        }
        return result;
    }

    public ArrayList<int[]> convertImageInvers(ArrayList<int[]> grayscaleImg){
        ArrayList<int[]> result = new ArrayList<>();
        for(int i = 0; i<grayscaleImg.size();i++) {
            int[] newRow = new int[grayscaleImg.get(i).length];
            for(int j = 0; j<grayscaleImg.get(i).length;j++) {
                if(grayscaleImg.get(i)[j]>=threshold) {
                    newRow[j] = 0;
                } else {
                    newRow[j] = 255;
                }
            }
            result.add(newRow);
        }
        return result;
    }
}
