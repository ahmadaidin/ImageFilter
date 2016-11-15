package com.example.ahmadaidin.imgfilter.Feature;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class FeatureFinder {

    private ArrayList<int[]> grayscale;
    private final int maxRadius=5;
    private final Point[][] nearPixel = generateNearPixelMtx(maxRadius);
    public FeatureFinder() {
        grayscale = new ArrayList<>();
    }

    public FeatureFinder(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }

    public void setGrayscale(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }

    public void iterate(ArrayList<Feature> features, Point iterator){
        setVisited(iterator);
        Feature paths = findPath(iterator);
        for(int i = 0; i<paths.pixelPosSize(); i++) {
            Feature feature = new Feature();
            feature.addPixelPos(iterator);
            feature.addDirection(paths.getDirectionElmt(i));
            Point start = new Point(paths.getPixelPosElmt(i));
            addPixelToFeature(feature, start);
            if(feature.pixelPosSize()!=0){
                features.add(feature);
            }
        }
    }

    public void addPixelToFeature(Feature feature, Point iterator) {
        feature.addPixelPos(iterator);
        Feature paths = findPath(iterator);
        while(paths.pixelPosSize()==1) {
            setVisited(iterator);
            iterator = new Point(paths.getPixelPosElmt(0));
            feature.addDirection(paths.getDirectionElmt(0));
            feature.addPixelPos(paths.getPixelPosElmt(0));
            paths = findPath(iterator);
            int radius = 2;
            if(paths.pixelPosSize()==0) {
                Log.d("apakah 0? "," ya");
                boolean found = false;
                while (!found && radius<=maxRadius) {
                    int start = maxRadius-radius;
                    int end = start + 2* radius;
                    for (int i = start; i <= end; i++) {
                        int j = start;
                        while (j<=end){
                            int newX = iterator.x + nearPixel[i][j].x;
                            int newY = iterator.y + nearPixel[i][j].y;
                            if (newY < 0 || newY >= grayscale.size())
                                continue;
                            if (newX < 0 || newX >= grayscale.get(newY).length)
                                continue;
                            if (grayscale.get(newY)[newX] == 255) {
                                Log.d("iterator",""+iterator.x+","+iterator.y);
                                Log.d("founded point",""+newX+","+newY);
                                Log.d("ketemu di radius",""+radius);
                                found = true;
                                Point p1 = new Point(iterator);
                                Point p2 = new Point(newX, newY);
                                Line line = new Line(p1, p2);
                                ArrayList<Point> linePoints = line.getAllPoints();
                                for (int k = 1; k < linePoints.size(); k++) {
                                    grayscale.get(linePoints.get(k).y)[linePoints.get(k).x] = 255;
                                }
                            }
                            if (i !=start && i !=end)
                                j+=radius*2;
                            else j++;
                            //if(found) break;
                        }
                       // if(found) break;
                    }
                    radius++;
                }
                if (!found)
                    Log.d("tidak ketemu","radius: "+radius);
            }
            paths = findPath(iterator);
        }
    }

    public void setVisited(Point p) {
        grayscale.get(p.y)[p.x]=200;
    }

    public Point[][] generateNearPixelMtx(int maxRadius){
        int size = maxRadius*2+1;
        Point[][] nearPixel = new Point[size][size];
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size;j++) {
                Point p = new Point(j-maxRadius,i-maxRadius);
                nearPixel[i][j] = p;
            }
        }
        return nearPixel;
    }

    public ArrayList<Feature> findFeatures() {
        ArrayList<Feature> features = new ArrayList<>();
        Point iterator = new Point(0,0);
        while (iterator.y < grayscale.size()) {
            while (iterator.x < grayscale.get(iterator.y).length) {
                if (grayscale.get(iterator.y)[iterator.x] == 255) {
//                    Log.d("ketemu objek",""+iterator.x+","+iterator.y);
                    Point foundStart = new Point(iterator);
                    iterate(features,iterator);
                    iterator.set(foundStart.x,foundStart.y);
                }
                iterator.offset(1, 0);
            }
            iterator.set(0,iterator.y+1);
        }
        Log.d("jumlah fitur:",""+features.size());
        return features;
    }

    public Feature findPath(Point iterator) {
        Feature paths = new Feature();
        Point searcher;
        if(grayscale.get(iterator.y-1)[iterator.x-1]==255){
            searcher= new Point(iterator);
            searcher.offset(-1,-1);
            paths.addPixelPos(searcher);
            paths.addDirection(1);
        }
        if (grayscale.get(iterator.y-1)[iterator.x]==255 ) {
            searcher= new Point(iterator);
            searcher.offset(0,-1);
            paths.addPixelPos(searcher);
            paths.addDirection(2);
        }
        if (grayscale.get(iterator.y-1)[iterator.x+1]==255 ) {
            searcher= new Point(iterator);
            searcher.offset(1,-1);
            paths.addPixelPos(searcher);
            paths.addDirection(3);
        }
        if (grayscale.get(iterator.y)[iterator.x+1]==255 ) {
            searcher= new Point(iterator);
            searcher.offset(1,0);
            paths.addPixelPos(searcher);
            paths.addDirection(4);
        }
        if (grayscale.get(iterator.y+1)[iterator.x+1]==255) {
            searcher= new Point(iterator);
            searcher.offset(1,1);
            paths.addPixelPos(searcher);
            paths.addDirection(5);
        }
        if (grayscale.get(iterator.y+1)[iterator.x]==255) {
            searcher= new Point(iterator);
            searcher.offset(0,1);
            paths.addPixelPos(searcher);
            paths.addDirection(6);
        }
        if (grayscale.get(iterator.y+1)[iterator.x-1]==255 ) {
            searcher= new Point(iterator);
            searcher.offset(-1,1);
            paths.addPixelPos(searcher);
            paths.addDirection(7);
        }
        if (grayscale.get(iterator.y)[iterator.x-1]==255) {
            searcher= new Point(iterator);
            searcher.offset(-1,0);
            paths.addPixelPos(searcher);
            paths.addDirection(8);
        }
        return paths;
    }

}
