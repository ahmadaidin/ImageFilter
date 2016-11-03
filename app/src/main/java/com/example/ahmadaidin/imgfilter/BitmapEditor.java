package com.example.ahmadaidin.imgfilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
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
    public int featuresNum;
    public int faceNum;

    public BitmapEditor() {

    }

    public BitmapEditor(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriBitmap = bitmap;
        setGrayscale();
        oriGrayscale = grayscale;
        setGreylevelHistogram();
        featuresNum=0;
    }

    public ArrayList<int[]> getGrayscale() {
        return grayscale;
    }

    public int[] getGrayHistogram() {
        return grayHistogram;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap);
        this.oriBitmap =bitmap;
        setGrayscale();
        oriGrayscale = grayscale;
        setGreylevelHistogram();
        featuresNum=0;
        faceNum = 0;
    }

    public Bitmap bitmap(){
        return bitmap;
    }

    public void resetBitmap() {
        bitmap = Bitmap.createBitmap(oriBitmap);
        resetGrayscale();
        featuresNum=0;
        faceNum = 0;
    }

    private void resetGrayscale() {
        for (int i = 0; i< oriGrayscale.size(); i++) {
            grayscale.set(i,oriGrayscale.get(i));
        }
    }

    public static ArrayList<int[]> getGrayscale(Bitmap bmp) {
        ArrayList<int[]> grayscale = new ArrayList<>(bmp.getWidth());


        for (int y=0; y< bmp.getHeight(); y++) {
            int[] row = new int[bmp.getWidth()];
            for (int x=0; x< bmp.getWidth(); x++) {
                int c = bmp.getPixel(x,y);
                int gray = Math.round((Color.red(c)+Color.green(c)+Color.blue(c))/3);
                row[x] = gray;
            }
            grayscale.add(row);
        }
        return grayscale;
    }



    public static int[] getGreylevelHistogram(Bitmap bmp) {
        ArrayList<int[]> grayscale = getGrayscale(bmp);

        int histogram[] = new int[256];
        for(int i=0; i<histogram.length; i++) histogram[i]=0;

        for(int y = 0; y < grayscale.size(); y++) {
            for (int x = 0; x<grayscale.get(y).length; x++) {
                int gray = grayscale.get(y)[x];
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

        for(int y=0; y<bmp.getHeight(); y++){
            for(int x=0; x<bmp.getWidth(); x++){
                int oldGray = grayscale.get(y)[x];
                int newGray = histLUT.get(oldGray);
                int deltaGray = newGray - oldGray;
                int p = bmp.getPixel(x,y);

                int r = Color.red(p)+deltaGray;
                int g = Color.green(p)+deltaGray;
                int b = Color.blue(p)+deltaGray;

                if (r<0){ r = 0;}
                else if(r>255){ r = 255; }

                if (g<0){ g = 0;}
                else if(g>255){ g = 255; }

                if (b<0){ b = 0;}
                else if(b>255){ b = 255; }

                histogramEQ.setPixel(x, y, Color.argb(Color.alpha(p),r,g,b));
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

        for(int y = 0; y < grayscale.size(); y++) {
            for (int x = 0; x<grayscale.get(x).length; x++) {
                int gray = grayscale.get(y)[x];
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

        for(int y=0; y<bitmap.getHeight(); y++){
            for(int x=0; x<bitmap.getWidth(); x++){
                int oldGray = grayscale.get(y)[x];
                int newGray = histLUT.get(oldGray);
                int deltaGray = newGray - oldGray;
                int p = bitmap.getPixel(x,y);

                bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void smoothImage() {

        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int sum = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        sum += grayscale.get(y+dy)[x+dx];
                    }
                }
                int avg = sum / 9;
                int oldGray = grayscale.get(y)[x];
                int deltaGray = avg - oldGray;
                int p = bitmap.getPixel(x,y);

                int r = Color.red(p)+deltaGray;
                int g = Color.green(p)+deltaGray;
                int b = Color.blue(p)+deltaGray;

                bitmap.setPixel(x, y, Color.argb(Color.alpha(p),r,g,b));
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

        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int sum = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        int c = grayscale.get(y+dy)[x+dx];
                        sum += filter[dx+1][dy+1]*c;
                    }
                }
                int oldGray = grayscale.get(y)[x];
                int deltaGray = sum - oldGray;
                int p = bitmap.getPixel(x,y);
                bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
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

        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int sum = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        int c = grayscale.get(y+dy)[x+dx];
                        sum += filter[dx+1][dy+1]*c;
                    }
                }
                int oldGray = grayscale.get(y)[x];
                int deltaGray = sum - oldGray;
                int p = bitmap.getPixel(x,y);
                bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void crossNbrs(){
        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int Opt1 = Math.abs(grayscale.get(y-1)[x-1]-grayscale.get(y+1)[x+1]);
                int Opt2 = Math.abs(grayscale.get(y)[x-1]-grayscale.get(y)[x+1]);
                int Opt3 = Math.abs(grayscale.get(y+1)[x-1]-grayscale.get(y-1)[x+1]);
                int Opt4 = Math.abs(grayscale.get(y-1)[x-1]-grayscale.get(y+1)[x+1]);

                int newGray = Math.max(Math.max(Math.max(Opt1,Opt2),Opt3),Opt4);

                int oldGray = grayscale.get(y)[x];
                int deltaGray = newGray - oldGray;
                int p = bitmap.getPixel(x,y);
                bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void centerNbrs(){
        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int max = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        int opt = Math.abs(grayscale.get(y)[x]-grayscale.get(y+dy)[x+dx]);
                        max = Math.max(max,opt);
                    }
                }
                int newGray = max;

                int oldGray = grayscale.get(y)[x];
                int deltaGray = newGray - oldGray;
                int p = bitmap.getPixel(x,y);
                bitmap.setPixel(x, y, manipulatePixel(p,deltaGray));
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

            for(int y=1; y<bitmap.getHeight()-1; y++){
                for(int x=1; x<bitmap.getWidth()-1; x++){
                    int sumH = 0, sumV = 0;
                    int size = hMatrix.size();
                    for (int dy = -1; dy<size-1; dy++) {
                        for(int dx = -1; dx<size-1; dx++) {
                            sumH += grayscale.get(y+dy)[x+dx]*hMatrix.get(dy+1).get(dx+1);
                            sumV += grayscale.get(y+dy)[x+dx]*vMatrix.get(dy+1).get(dx+1);
                        }
                    }
                    int newGray =toPositive(sumH)+toPositive(sumV);
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
                    for (int dy = -1; dy<size-1; dy++) {
                        for(int dx = -1; dx<size-1; dx++) {
                            sumH += grayscale.get(y+dy)[x+dx]*hMatrix.get(dy+1).get(dx+1);
                            sumV += grayscale.get(y+dy)[x+dx]*vMatrix.get(dy+1).get(dx+1);
                        }
                    }
                    int newGray = toPositive(sumH) + toPositive(sumV);
                    int oldGray = grayscale.get(y)[x];
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
                    for (int dy = -1; dy<size-1; dy++) {
                        for(int dx = -1; dx<size-1; dx++) {
                            sumN += grayscale.get(y+dy)[x+dx]*north.get(dy+1).get(dx+1);
                            sumE += grayscale.get(y+dy)[x+dx]*east.get(dy+1).get(dx+1);
                            sumS += grayscale.get(y+dy)[x+dx]*south.get(dy+1).get(dx+1);
                            sumW += grayscale.get(y+dy)[x+dx]*west.get(dy+1).get(dx+1);
                            sumNE += grayscale.get(y+dy)[x+dx]*northEast.get(dy+1).get(dx+1);
                            sumSE += grayscale.get(y+dy)[x+dx]*southEast.get(dy+1).get(dx+1);
                            sumSW += grayscale.get(y+dy)[x+dx]*southWest.get(dy+1).get(dx+1);
                            sumNW += grayscale.get(y+dy)[x+dx]*northWest.get(dy+1).get(dx+1);
                        }
                    }
                    int newGray =toPositive(sumN)+toPositive(sumE)+toPositive(sumS)+toPositive(sumW)+toPositive(sumNE)+toPositive(sumSE)+toPositive(sumSW)+toPositive(sumNW);
                    int oldGray = grayscale.get(y)[x];
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

    public void binaryConvert(BinaryConverter bC){
        ArrayList<int[]> newGray= bC.convertImage(this.getGrayscale());
        this.grayscale = newGray;
        for(int y=1; y<bitmap.getHeight()-1; y++) {
            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
                int val = grayscale.get(y)[x];
                bitmap.setPixel(x,y,Color.rgb(val,val,val));
            }
        }
    }

    public void binaryConvertInvers(BinaryConverter bC){
        this.grayscale = bC.convertImageInvers(this.getGrayscale());
        for(int y=1; y<bitmap.getHeight()-1; y++) {
            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
                int val = grayscale.get(y)[x];
                bitmap.setPixel(x,y,Color.rgb(val,val,val));
            }
        }
    }

    public void skeletonize(){
        ZhangSuenSkeletonizer skeletonizer = new ZhangSuenSkeletonizer(grayscale);
        skeletonizer.skeletonize();
        for(int y=1; y<bitmap.getHeight()-1; y++) {
            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
                int val = grayscale.get(y)[x];
                bitmap.setPixel(x,y,Color.rgb(val,val,val));
            }
        }
    }

    public void extractFeature(){
        FeatureFinder featureFinder = new FeatureFinder(this.grayscale);
        ArrayList<Feature> features = featureFinder.findFeatures();
        featuresNum = features.size();
        for(int y=0; y<bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                bitmap.setPixel(x,y,Color.rgb(0,0,0));
            }
        }
        for(int i = 0; i<features.size(); i++) {
            printFeatures(features.get(i));
        }
    }

    public void printFeatures(Feature feature) {
        for(int i =0; i<feature.pixelPosSize(); i++) {
            bitmap.setPixel(feature.getPixelPosElmt(i).x,feature.getPixelPosElmt(i).y,Color.rgb(255,255,0));
        }
    }

    public void detectFace() {
        MyFaceDetector faceDetector = new MyFaceDetector(bitmap);
        faceDetector.detectFace();
        faceNum = faceDetector.getFaceNum();
    }

    private  int toPositive(int a){
        return Math.max(a,-1*a);
    }

}
