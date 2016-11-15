package com.example.ahmadaidin.imgfilter;
import android.graphics.Color;
import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Aidin - 2 on 25/09/2016.
 */

public class ColorReader {
    private ArrayList<ArrayList<Integer>> matrix;
    private InputStream in;

    public ColorReader(InputStream in) {
        this.in = in;
        try {
            matrix = readJsonStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer>getColorsMatrix(){
        ArrayList<Integer> color = new ArrayList<>();
        for(int i=0; i<matrix.size(); i++) {
            int r = matrix.get(i).get(0);
            int g = matrix.get(i).get(1);
            int b = matrix.get(i).get(2);
            int c = Color.rgb(r,g,b);
            color.add(c);
        }
        return color;
    }

    private ArrayList<ArrayList<Integer>> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMatrix(reader);
        } finally {
            reader.close();
        }
    }


    private ArrayList<ArrayList<Integer>> readMatrix(JsonReader reader) throws IOException {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()) {
            matrix.add(readArray(reader));
        }
        reader.endArray();
        return matrix;
    }

    private  ArrayList<Integer> readArray(JsonReader reader) throws  IOException {
        ArrayList<Integer> array = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()) {
            array.add(reader.nextInt());
        }
        reader.endArray();
        return array;
    }
}
