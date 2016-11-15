package com.example.ahmadaidin.imgfilter.ObjectDetector;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.example.ahmadaidin.imgfilter.ObjectDetector.FaceDetector;

import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 02/11/2016.
 */

public class MyFaceDetector extends FaceDetector {
    private static final int visitedColor = 255;

    final static int[][] nbrs = {
            {-1, -1}/*1*/, {0, -1}/*2*/, {1, -1}/*3*/,
            {1, 0}/*4*/, {1, 1}/*5*/, {0, 1}/*6*/,
            {-1, 1}/*7*/,{-1, 0}/*8*/
    };

    public MyFaceDetector(Bitmap bitmap){
        super(bitmap);
    }

    public void detectFace(){
        convertImage();
        findSkin();
        plotSkin();
    }

    public void convertImage() {
        for(int y = 0; y<bitmap.getHeight(); y++) {
            for (int x = 0; x<bitmap.getWidth(); x++) {
                if (hasSkinColor(x,y)) {
                    bitmap.setPixel(x,y, Color.rgb(255,255,255));
                } else {
                    bitmap.setPixel(x,y,Color.rgb(0,0,0));
                }
            }
         }
    }

    public void findSkin(){
        for(int y=0; y<bitmap.getHeight(); y++) {
            for(int x=0; x<bitmap.getWidth(); x++) {
                if(!isVisited(x,y)) { //jika belum dilewati
                    ArrayList<Point> skin = new ArrayList<>();
                    checkNbrs(skin,x,y);
                    if(skin.size()!=0){
                        faces.add(skin);
                    }
                }
            }
        }
    }

    public void checkNbrs(ArrayList<Point> skin, int x, int y){//cek 8 arah pixel yang lain hingga tidak menemukan yang bersebelahan
        if(x>=bitmap.getWidth() || y>=bitmap.getHeight() || x<0 || y<0){//lewat batas
            //do nothing
        } else {
            if(isVisited(x,y)) {//sudah dikunjungi
                //do nothing
            } else{ //belum dikunjungi
                if (isWhite(x, y)) {//punya warna kulit
                    setVisited(x, y);
                    skin.add(new Point(x, y));
                    for (int i = 0; i < nbrs.length; i++) {
                        checkNbrs(skin, x + nbrs[i][0], y + nbrs[i][1]);
                    }
                } else setVisited(x, y);
            }
        }
    }
    public void plotSkin() {
        bitmap = Bitmap.createBitmap(oriBitmap);
        for(int i = 0; i<faces.size(); i++) {
            int xMin, xMax, yMin, yMax;
            xMin = xMax = faces.get(i).get(0).x;
            yMin = yMax = faces.get(i).get(0).y;
            for(int j = 1; j < faces.get(i).size(); j++) {
                Point pixel = faces.get(i).get(j);
                if(pixel.x < xMin) xMin = pixel.x;
                else if(pixel.x > xMax) xMax = pixel.x;
                if(pixel.y < yMin) yMin = pixel.y;
                else if(pixel.y > yMax) yMax = pixel.y;
            }

            int y, x;
            y = yMin;
            for(x = xMin; x<=xMax; x++){
                bitmap.setPixel(x,y,Color.rgb(255,0,0));
            }
            y = yMax;
            for(x = xMin; x<=xMax; x++){
                bitmap.setPixel(x,y,Color.rgb(255,0,0));
            }
            x = xMin;
            for(y = yMin; y<=yMax; y++){
                bitmap.setPixel(x,y,Color.rgb(255,0,0));
            }
            x = xMax;
            for(y = yMin; y<=yMax; y++){
                bitmap.setPixel(x,y,Color.rgb(255,0,0));
            }
        }
    }


    public boolean hasSkinColor(int x, int y){
        int c = bitmap.getPixel(x, y);
        int R = Color.red(c);
        int G = Color.green(c);
        int B = Color.blue(c);

        //double Y =  0.299*R + 0.587*G + 0.114*B;
        double Cb = -0.169*R - 0.332*G + 0.500*B;
        double Cr = 0.500*R - 0.419*G - 0.081*B;
        boolean isCbSkin = (Cb>=-20 && Cb<=-5);
        boolean isCrSkin = (Cr>=15 && Cr<=33);

        return isCbSkin && isCrSkin;
    }

    public void setVisited(int x, int y){
        bitmap.setPixel(x,y,Color.rgb(visitedColor,0,0));
    }

    public boolean isVisited(int x, int y) {
        return bitmap.getPixel(x,y)==Color.rgb(visitedColor,0,0);
    }

    public boolean isWhite(int x, int y) {
        return bitmap.getPixel(x,y)==Color.rgb(255,255,255);
    }

}
