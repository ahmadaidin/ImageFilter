package com.example.ahmadaidin.imgfilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private Bitmap oriBitmap;
    private ArrayList<int[]> grayscale;
    private ArrayList<int[]> oriGrayscale;
    private int[] grayHistogram;
    private Hashtable<Integer, Integer> convolusionMatrix;

    public BitmapEditor() {

    }

    public BitmapEditor(Bitmap bitmap){
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriBitmap = bitmap;
        setGrayscale();
        oriGrayscale = grayscale;
        setGreylevelHistogram();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriBitmap =bitmap;
        setGrayscale();
        oriGrayscale = grayscale;
        setGreylevelHistogram();
    }

    public Bitmap bitmap(){
        return bitmap;
    }

    public void resetBitmap() {
        bitmap = Bitmap.createBitmap(oriBitmap);
        resetGrayscale();
    }

    private void resetGrayscale() {
        for (int i = 0; i< oriGrayscale.size(); i++) {
            grayscale.set(i,oriGrayscale.get(i));
        }
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

                bitmap.setPixel(i, j, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
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
        setGrayscale();
    }

    public void sharpImage(){
        int[][] filter  = {
                {0,-1,0},
                {-1,5,-1},
                {0,-1,0}
        };

        for(int i=1; i<bitmap.getHeight()-1; i++){
            for(int j=1; j<bitmap.getWidth()-1; j++){
                int sum = 0;
                for (int x = -1; x<2; x++) {
                    for(int y = -1; y<2; y++) {
                        int c = grayscale.get(j+x)[i+y];
                        sum += filter[x+1][y+1]*c;
                    }
                }
                int oldGray = grayscale.get(j)[i];
                int deltaGray = sum - oldGray;
                int p = bitmap.getPixel(j,i);
                bitmap.setPixel(j, i, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void blurImage(){
        double[][] filter  = {
                {0.0,0.2,0.0},
                {0.2,0.2,0.2},
                {0.0,0.2,0.0}
        };

        for(int i=1; i<bitmap.getHeight()-1; i++){
            for(int j=1; j<bitmap.getWidth()-1; j++){
                int sum = 0;
                for (int x = -1; x<2; x++) {
                    for(int y = -1; y<2; y++) {
                        int c = grayscale.get(j+x)[i+y];
                        sum += filter[x+1][y+1]*c;
                    }
                }
                int oldGray = grayscale.get(j)[i];
                int deltaGray = sum - oldGray;
                int p = bitmap.getPixel(j,i);
                bitmap.setPixel(j, i, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void detectEdge1(){
        for(int i=1; i<bitmap.getHeight()-1; i++){
            for(int j=1; j<bitmap.getWidth()-1; j++){
                int Opt1 = Math.abs(grayscale.get(j-1)[i-1]-grayscale.get(j+1)[i+1]);
                int Opt2 = Math.abs(grayscale.get(j)[i-1]-grayscale.get(j)[i+1]);
                int Opt3 = Math.abs(grayscale.get(j+1)[i-1]-grayscale.get(j-1)[i+1]);
                int Opt4 = Math.abs(grayscale.get(j-1)[i-1]-grayscale.get(j+1)[i+1]);

                int newGray = Math.max(Math.max(Math.max(Opt1,Opt2),Opt3),Opt4);

                int oldGray = grayscale.get(j)[i];
                int deltaGray = newGray - oldGray;
                int p = bitmap.getPixel(j,i);
                bitmap.setPixel(j, i, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void detectEdge2(){
        for(int i=1; i<bitmap.getHeight()-1; i++){
            for(int j=1; j<bitmap.getWidth()-1; j++){
                int max = 0;
                for (int x = -1; x<2; x++) {
                    for(int y = -1; y<2; y++) {
                        int opt = Math.abs(grayscale.get(j)[i]-grayscale.get(j+x)[i+y]);
                        max = Math.max(max,opt);
                    }
                }
                int newGray = max;

                int oldGray = grayscale.get(j)[i];
                int deltaGray = newGray - oldGray;
                int p = bitmap.getPixel(j,i);
                bitmap.setPixel(j, i, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public  void robert(Convolution c) {
        ArrayList<ArrayList<Integer>> hMatrix;
        ArrayList<ArrayList<Integer>> vMatrix;
        try {
            hMatrix = c.getMatrix("Robert");
            vMatrix = MatrixOperator.rotateLeft(hMatrix);

            for(int i=0; i<bitmap.getHeight()-1; i++){
                for(int j=0; j<bitmap.getWidth()-1; j++){
                    int sumH = 0, sumV = 0;

                    for (int y = 0; y<hMatrix.size(); y++) {
                        for(int x = 0; x<hMatrix.get(y).size(); x++) {
                            sumH += grayscale.get(j+y)[i+x]*hMatrix.get(y).get(x);
                            sumV += grayscale.get(j+y)[i+x]*vMatrix.get(y).get(x);
                        }
                    }
                    int newGray =toPositive(sumH)+toPositive(sumV);
                    int oldGray = grayscale.get(j)[i];
                    int deltaGray = newGray - oldGray;
                    int p = bitmap.getPixel(j,i);
                    bitmap.setPixel(j, i, manipulatePixel(p,deltaGray));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGrayscale();
    }

    public void edgeDetectLv1(Convolution c, String Operator) {
        ArrayList<ArrayList<Integer>> hMatrix;
        ArrayList<ArrayList<Integer>> vMatrix;
        try {
            hMatrix = c.getMatrix(Operator);
            vMatrix = MatrixOperator.rotateLeft(hMatrix);

            for(int y=1; y<bitmap.getHeight()-1; y++){
                for(int x=1; x<bitmap.getWidth()-1; x++){
                    int sumH = 0, sumV = 0;
                    int size = hMatrix.size();
                    for (int i = -1; i<size-1; i++) {
                        for(int j = -1; j<size-1; j++) {
                            sumH += grayscale.get(x+i)[y+j]*hMatrix.get(i+1).get(j+1);
                            sumV += grayscale.get(x+i)[y+j]*vMatrix.get(i+1).get(j+1);
                        }
                    }
                    int newGray = toPositive(sumH)+toPositive(sumV);
                    int oldGray = grayscale.get(x)[y];
                    int deltaGray = newGray - oldGray;
                    int p = bitmap.getPixel(x,y);
                    bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGrayscale();
    }

    public  void edgeDetectLv2(Convolution c, String Operator) {
        ArrayList<ArrayList<Integer>> north;
        ArrayList<ArrayList<Integer>> west;
        ArrayList<ArrayList<Integer>> south;
        ArrayList<ArrayList<Integer>> east;
        ArrayList<ArrayList<Integer>> northEast;
        ArrayList<ArrayList<Integer>> southEast;
        ArrayList<ArrayList<Integer>> southWest;
        ArrayList<ArrayList<Integer>> northWest;


        try {
            north = c.getMatrix(Operator);
            west = MatrixOperator.rotateLeft(north);
            south = MatrixOperator.rotateLeft(west);
            east = MatrixOperator.rotateLeft(south);
            northEast = MatrixOperator.rotate1Right(north);
            southEast = MatrixOperator.rotate1Right(east);
            southWest = MatrixOperator.rotate1Right(south);
            northWest = MatrixOperator.rotate1Right(west);


            for(int y=1; y<bitmap.getHeight()-1; y++){
                for(int x=1; x<bitmap.getWidth()-1; x++){
                    int sumN = 0, sumE = 0, sumS=0, sumW=0, sumNE=0, sumSE=0, sumSW=0, sumNW=0;
                    int size = north.size();
                    for (int i = -1; i<size-1; i++) {
                        for(int j = -1; j<size-1; j++) {
                            sumN += grayscale.get(x+i)[y+j]*north.get(i+1).get(j+1);
                            sumE += grayscale.get(x+i)[y+j]*east.get(i+1).get(j+1);
                            sumS += grayscale.get(x+i)[y+j]*south.get(i+1).get(j+1);
                            sumW += grayscale.get(x+i)[y+j]*west.get(i+1).get(j+1);
                            sumNE += grayscale.get(x+i)[y+j]*northEast.get(i+1).get(j+1);
                            sumSE += grayscale.get(x+i)[y+j]*southEast.get(i+1).get(j+1);
                            sumSW += grayscale.get(x+i)[y+j]*southWest.get(i+1).get(j+1);
                            sumNW += grayscale.get(x+i)[y+j]*northWest.get(i+1).get(j+1);
                        }
                    }
                    int newGray =toPositive(sumN)+toPositive(sumE)+toPositive(sumS)+toPositive(sumW)+toPositive(sumNE)+toPositive(sumSE)+toPositive(sumSW)+toPositive(sumNW);
                    int oldGray = grayscale.get(x)[y];
                    int deltaGray = newGray - oldGray;
                    int p = bitmap.getPixel(x,y);
                    bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGrayscale();
    }

    private int manipulatePixel(int pixel, int addition) {
        int  newPixel;

        int r = Color.red(pixel)+addition;
        int g = Color.green(pixel)+addition;
        int b = Color.blue(pixel)+addition;

        r = Math.min(Math.max(0,r),255);
        g = Math.min(Math.max(0,g),255);
        b = Math.min(Math.max(0,b),255);

        newPixel = Color.argb(Color.alpha(pixel),r,g,b);

        return newPixel;
    }

    private  int toPositive(int a){
        return Math.max(a,-1*a);
    }

}
