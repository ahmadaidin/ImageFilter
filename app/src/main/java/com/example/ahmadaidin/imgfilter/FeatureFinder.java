package com.example.ahmadaidin.imgfilter;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class FeatureFinder {

    private ArrayList<int[]> grayscale;
    private ArrayList<Feature> features;

    public FeatureFinder() {
        grayscale = new ArrayList<>();
    }

    public FeatureFinder(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }

    public void setGrayscale(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }

    public void iterate(ArrayList<Feature> features, Point iterator, int repetition){
        Feature paths;
        Feature feature = new Feature();
        if (grayscale.get(iterator.y)[iterator.y] == 255) {
            int ignoredColor = 255;
            while(grayscale.get(iterator.y)[iterator.x]!=0) {
                feature.addPixelPos(new Point(iterator));
                paths = findPath(ignoredColor,iterator);
                if(paths.directionSize()==1){
                    feature.addPixelPos(paths.getPixelPosElmt(0));
                    feature.addDirection(paths.getDirectionElmt(0));
                    int[] row = grayscale.get(paths.getPixelPosElmt(0).y);
                    row[paths.getPixelPosElmt(0).x] = 200;
                } else {
                    if (repetition == 0) {
                        feature.clear();
                        repetition++;
                        ignoredColor = 200;
                    } else {
                        ignoredColor = 255;
                        features.add(feature);
                    }

                    if(paths.directionSize()==0) {
                        //
                    } else if(paths.directionSize()>1){
                        for(int i = 0; i<paths.pixelPosSize(); i++) {
                            Point start = new Point(iterator);
                            iterate(features,start,repetition);
                        }
                    }
                }
            }
        }
    }

    public Feature findPath(int ignoredColor, Point iterator) {
        Feature paths = new Feature();
        Point searcher = new Point(iterator);
        if(grayscale.get(iterator.y-1)[iterator.x-1]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor){
            searcher.offset(-1,-1);
            paths.addPixelPos(searcher);
            paths.addDirection(1);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y-1)[iterator.x]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(0,-1);paths.addPixelPos(searcher);
            paths.addDirection(2);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y-1)[iterator.x+1]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(1,-1);
            paths.addPixelPos(searcher);
            paths.addDirection(3);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y)[iterator.x+1]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(1,0);
            paths.addPixelPos(searcher);
            paths.addDirection(4);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y+1)[iterator.x+1]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(1,1);
            paths.addPixelPos(searcher);
            paths.addDirection(5);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y+1)[iterator.x]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(0,1);
            paths.addPixelPos(searcher);
            paths.addDirection(6);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y+1)[iterator.x-1]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(-1,1);
            paths.addPixelPos(searcher);
            paths.addDirection(7);
            searcher.set(iterator.x,iterator.y);
        }
        if (grayscale.get(iterator.y)[iterator.x-1]==255 || grayscale.get(iterator.y-1)[iterator.x-1]==ignoredColor) {
            searcher.offset(-1,0);
            paths.addPixelPos(searcher);
            paths.addDirection(8);
            searcher.set(iterator.x,iterator.y);
        }
        return paths;
    }

    public Point findObject(Point defaultReturn) {
        Point iterator = new Point();
        iterator.set(grayscale.get(0).length/2,0);
        boolean found = false;
        while(!found && iterator.y<grayscale.size()) {
            if(grayscale.get(iterator.y)[iterator.x]==255) {
                found = true;
            } else {
                iterator.offset(0, 1);
            }
        }
        iterator.set(0,grayscale.size()/2);
        while(!found && iterator.x<grayscale.get(grayscale.size()/2).length) {
            if(grayscale.get(iterator.y)[iterator.x]==255) {
                found = true;
            } else {
                iterator.offset(1, 0);
            }
        }
        if(!found) {
            return defaultReturn;
        } else {
            return iterator;
        }
    }

}
