package com.example.ahmadaidin.imgfilter;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Aidin Ahmad on 02/11/2016.
 */

public class MyFaceDetector extends FaceDetector {
    private ArrayList <Integer> faceColor;
    final int threshold = 5;

    public MyFaceDetector(Bitmap bitmap){
        super(bitmap);
        faceColor.add(Color.rgb(141,85,36));
        faceColor.add(Color.rgb(198,143,66));
        faceColor.add(Color.rgb(224,172,105));
        faceColor.add(Color.rgb(241,194,125));
        faceColor.add(Color.rgb(255,219,172));
        int size = faceColor.size();
        for (int i = 0; i<size; i++) {
            for (int j = 1; j <= threshold; j++) {
                faceColor.add(faceColor.get(i) + j);
                faceColor.add(faceColor.get(i) - j);
            }
        }
    }

    public void detectFace(){
        int x = 0;
        int y = 0;
        int countface = 0;
        int faceHeight =0;
        boolean found = false;
        boolean sameFace = true;
        while(y < bitmap.getHeight()) {
            int border = 0;
            while(x < bitmap.getWidth()) {
                if (border <= 1) {
                    int y1 = y;
                    findFaceColor(x, y, found);
                    if (found) {
                        int h = y1-y;
                        if(h==1){
                            faceHeight++;
                        } else{
                            if(true){

                            }
                        }

                    } else {

                    }
                } else {

                }
            }
        }


        if(true) sameFace = true;
        if(!sameFace) countface++;

    }

    public void findFaceColor(int x, int y, boolean found){
        found = false;
        while (!found && y< bitmap.getHeight()){
            while(!found && x< bitmap.getWidth()) {
                int i = 0;
                while(!found && i < faceColor.size()) {
                    if(bitmap.getPixel(x,y)==faceColor.get(i)){
                        found = true;
                    }
                    i++;
                }
                if (!found)
                    x++;
            }
            if (!found)
                y++;
        }
    }
}
