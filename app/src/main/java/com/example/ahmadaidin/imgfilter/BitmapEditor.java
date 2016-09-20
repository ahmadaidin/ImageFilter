package com.example.ahmadaidin.imgfilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Ahmad Aidin on 06/09/2016.
 */
public class BitmapEditor{
    @SuppressWarnings("unused")
    private static final String TAG = "image.filter.BitmapEditor";
    private Bitmap bitmap;
    private ArrayList<int[]> grayscale;
    private int[] grayHistogram;

    public BitmapEditor() {

    }

    public BitmapEditor(Bitmap bitmap){
        this.bitmap = Bitmap.createBitmap(bitmap);
        setGrayscale();
        setGreylevelHistogram();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
        setGrayscale();
        setGreylevelHistogram();
    }

    public Bitmap bitmap(){
        return bitmap;
    }

    public static ArrayList<int[]> getGrayscale(Bitmap bmp) {
        ArrayList<int[]> grayscale = new ArrayList<>(bmp.getWidth());


        for (int i=0; i< bmp.getWidth(); i++) {
            int[] col = new int[bmp.getHeight()];
            for (int j=0; j< bmp.getHeight(); j++) {
                int c = bmp.getPixel(i,j);
                int gray = Math.round((Color.red(c)+Color.green(c)+Color.blue(c))/3);
                col[j] = gray;
            }
            grayscale.add(col);
        }
        return grayscale;
    }



    public static int[] getGreylevelHistogram(Bitmap bmp) {
        ArrayList<int[]> grayscale = getGrayscale(bmp);

        int histogram[] = new int[256];
        for(int i=0; i<histogram.length; i++) histogram[i]=0;

        for(int i = 0; i < grayscale.size(); i++) {
            for (int j = 0; j<grayscale.get(i).length; j++) {
                int gray = grayscale.get(i)[j];
                histogram[gray]++;
            }
        }
        return histogram;
    }


    private  static Hashtable<Integer,Integer> getGrayCDF(Bitmap bmp){
        int histogram[] = getGreylevelHistogram(bmp);

        Hashtable<Integer,Integer> cdfTable = new Hashtable<>();

        int temp=0;
        for (int i = 0; i<histogram.length;i++) {
            if (histogram[i] != 0) {
                temp += histogram[i];
                cdfTable.put(i,temp);
            }
        }
        return cdfTable;
    }



    private static Hashtable<Integer, Integer> grayLvHistogramEqualizationLUT(Bitmap bmp) {
        Hashtable<Integer,Integer> cdfTable = getGrayCDF(bmp);
        Hashtable<Integer,Integer> imageLUT = new Hashtable<>();

        Enumeration<Integer> cdfEnum = cdfTable.elements();
        Enumeration<Integer> keyEnum = cdfTable.keys();

        int cdfMin = cdfEnum.nextElement();
        while(cdfEnum.hasMoreElements()){
            int val = cdfEnum.nextElement();
            if(val<cdfMin){
                cdfMin = val;
            }
        }

        long numPixel = bmp.getWidth() * bmp.getHeight();

        long denumerator = numPixel-cdfMin;
        while (keyEnum.hasMoreElements()) {
            int key = keyEnum.nextElement();
            int numerator = (cdfTable.get(key) - cdfMin) * (256 - 1);
            long val = (long) (Math.round(numerator/denumerator) + 0.5);
            if (val > 255) {
                imageLUT.put(key, 255);
            } else {
                imageLUT.put(key, (int) val);
            }
        }

        return imageLUT;
    }


    public static Bitmap grayLvHistogramEqualization(Bitmap bmp){

        Hashtable<Integer,Integer> histLUT = grayLvHistogramEqualizationLUT(bmp);
        ArrayList<int[]> grayscale = getGrayscale(bmp);

        Bitmap histogramEQ = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int oldGray = grayscale.get(i)[j];
                int newGray = histLUT.get(oldGray);
                int deltaGray = newGray - oldGray;
                int p = bmp.getPixel(i,j);

                int r = Color.red(p)+deltaGray;
                int g = Color.green(p)+deltaGray;
                int b = Color.blue(p)+deltaGray;

                if (r<0){ r = 0;}
                else if(r>255){ r = 255; }

                if (g<0){ g = 0;}
                else if(g>255){ g = 255; }

                if (b<0){ b = 0;}
                else if(b>255){ b = 255; }

                histogramEQ.setPixel(i, j, Color.argb(Color.alpha(p),r,g,b));
            }
        }

        return histogramEQ;
    }



    private void setGrayscale() {
        grayscale = getGrayscale(bitmap);
    }
    private void setGreylevelHistogram() {
        grayHistogram = new int[256];
        for(int i=0; i<grayHistogram.length; i++) grayHistogram[i]=0;

        for(int i = 0; i < grayscale.size(); i++) {
            for (int j = 0; j<grayscale.get(i).length; j++) {
                int gray = grayscale.get(i)[j];
                grayHistogram[gray]++;
            }
        }
    }

    private Hashtable<Integer,Integer> getGrayCDF(){
        Hashtable<Integer,Integer> grayCDF = new Hashtable<>();
        int temp=0;
        for (int i = 0; i<grayHistogram.length;i++) {
            if (grayHistogram[i] != 0) {
                temp += grayHistogram[i];
                grayCDF.put(i,temp);
            }
        }

        return grayCDF;
    }

    private Hashtable<Integer, Integer> grayLvHistogramEqualizationLUT() {
        Hashtable<Integer,Integer> imageLUT = new Hashtable<>();

        Hashtable<Integer,Integer> grayCDF = getGrayCDF();

        Enumeration<Integer> cdfEnum = grayCDF.elements();
        Enumeration<Integer> keyEnum = grayCDF.keys();

        int cdfMin = cdfEnum.nextElement();
        while(cdfEnum.hasMoreElements()){
            int val = cdfEnum.nextElement();
            if(val<cdfMin){
                cdfMin = val;
            }
        }

        long numPixel = bitmap.getWidth() * bitmap.getHeight();

        long denumerator = numPixel-cdfMin;
        while (keyEnum.hasMoreElements()) {
            int key = keyEnum.nextElement();
            int numerator = (grayCDF.get(key) - cdfMin) * (256 - 1);
            long val = (long) (Math.round(numerator/denumerator) + 0.5);
            if (val > 255) {
                imageLUT.put(key, 255);
            } else {
                imageLUT.put(key, (int) val);
            }
        }

        return imageLUT;
    }

    public void grayLvHistogramEqualization(){

        Hashtable<Integer,Integer> histLUT = grayLvHistogramEqualizationLUT();

        for(int i=0; i<bitmap.getWidth(); i++){
            for(int j=0; j<bitmap.getHeight(); j++){
                int oldGray = grayscale.get(i)[j];
                int newGray = histLUT.get(oldGray);
                int deltaGray = newGray - oldGray;
                int p = bitmap.getPixel(i,j);

                int r = Color.red(p)+deltaGray;
                int g = Color.green(p)+deltaGray;
                int b = Color.blue(p)+deltaGray;

                if (r<0){ r = 0;}
                else if(r>255){ r = 255; }

                if (g<0){ g = 0;}
                else if(g>255){ g = 255; }

                if (b<0){ b = 0;}
                else if(b>255){ b = 255; }

                bitmap.setPixel(i, j, Color.argb(Color.alpha(p),r,g,b));
            }
        }

    }

    public void smoothImage() {

        for(int i=1; i<bitmap.getWidth()-1; i++){
            for(int j=1; j<bitmap.getHeight()-1; j++){
                int sum = 0;
                for (int x = -1; x<2; x++) {
                    for(int y = -1; y<2; y++) {
                        sum += grayscale.get(i+x)[j+y];
                    }
                }
                int avg = sum / 9;
                int oldGray = grayscale.get(i)[j];
                int deltaGray = avg - oldGray;
                int p = bitmap.getPixel(i,j);

                int r = Color.red(p)+deltaGray;
                int g = Color.green(p)+deltaGray;
                int b = Color.blue(p)+deltaGray;

                bitmap.setPixel(i, j, Color.argb(Color.alpha(p),r,g,b));
            }
        }
    }

    public void sharpImage(){
        int[][] filter  = {
                {0,-1,0},
                {-1,5,-1},
                {0,-1,0}
        };

        for(int i=1; i<bitmap.getHeight()-1; i++){
            for(int j=1; j<bitmap.getWidth()-1; j++){
                int sumR = 0, sumG = 0, sumB = 0;
                int sum = 0;
                for (int x = -1; x<2; x++) {
                    for(int y = -1; y<2; y++) {
                        int c = grayscale.get(j+x)[i+y];
                        sum += filter[x+1][y+1]*c;
                        //sumR += filter[x+1][y+1]*Color.red(c);
                        //sumG += filter[x+1][y+1]*Color.green(c);
                        //sumB += filter[x+1][y+1]*Color.blue(c);
                    }
                }
                /*
                int r= sumR;
                int g= sumG;
                int b= sumB;
*/
                int oldGray = grayscale.get(j)[i];
                int deltaGray = sum - oldGray;
                int p = bitmap.getPixel(j,i);

                int r = Color.red(p)+deltaGray;
                int g = Color.green(p)+deltaGray;
                int b = Color.blue(p)+deltaGray;

                if (r<0){ r = 0;}
                else if(r>255){ r = 255; }

                if (g<0){ g = 0;}
                else if(g>255){ g = 255; }

                if (b<0){ b = 0;}
                else if(b>255){ b = 255; }

                bitmap.setPixel(j, i, Color.argb(Color.alpha(p),r,g,b));
            }
        }
    }



}
