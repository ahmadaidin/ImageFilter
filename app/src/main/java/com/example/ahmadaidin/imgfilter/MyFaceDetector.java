package com.example.ahmadaidin.imgfilter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 02/11/2016.
 */

public class MyFaceDetector extends FaceDetector {
    private int[] skinColors;
    private ArrayList<ArrayList<Point>> skins;
    private int visitedColor;
    final static int[][] nbrs = {
            {-1, -1}/*1*/, {0, -1}/*2*/, {1, -1}/*3*/,
            {1, 0}/*4*/, {1, 1}/*5*/, {0, 1}/*6*/,
            {-1, 1}/*7*/,{-1, 0}/*8*/
    };

    public MyFaceDetector(Bitmap bitmap){
        super(bitmap);
        skinColors = new int[0];
        visitedColor = 0;
    }

    public void detectFace(){

    }

    public void findSkin(){
        for(int y=0; y<bitmap.getHeight(); y++) {
            for(int x=0; x<bitmap.getWidth(); x++) {
                if(!isVisited(x,y)) { //cek sudah dilewati atau belum, eksekusi jika belum dilewati
                    ArrayList<Point> skin = new ArrayList<>();
                    checkNbrs(skin,x,y);
                    if(skin.size()!=0){
                        skins.add(skin);
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
                setVisited(x, y);
                if (hasSkinColor(x, y)) {//punya warna kulit
                    skin.add(new Point(x, y));
                    for (int i = 0; i < nbrs.length; i++) {
                        checkNbrs(skin, x + nbrs[i][0], y + nbrs[i][1]);
                    }
                }
            }
        }
    }

    public boolean hasSkinColor(int x, int y){
        boolean found = false;
        int i=0;
        while (!found && i < skinColors.length) {//pixel dicek warnanya apakah
            // sama seperti warna kulit atau tidak
            if (bitmap.getPixel(x, y) == skinColors[i]) found = true;
            i++;
        }

        return found;
    }

    public void setVisited(int x, int y){
        bitmap.setPixel(x,y,0);
    }

    public boolean isVisited(int x, int y) {
        return bitmap.getPixel(x,y)==0;
    }

}
