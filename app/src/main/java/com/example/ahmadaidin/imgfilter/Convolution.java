package com.example.ahmadaidin.imgfilter;
import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Aidin - 2 on 25/09/2016.
 */

public class Convolution {
    private Hashtable<String, ArrayList<ArrayList<Integer>>> allMatrix;
    private InputStream in;

    public Convolution(InputStream in) {
        this.in = in;
        try {
            allMatrix = readJsonStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Integer>> getMatrix(String name) throws Exception{
        return allMatrix.get(name);
    }

    private Hashtable<String, ArrayList<ArrayList<Integer>>> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessages(reader);
        } finally {
            reader.close();
        }
    }

    private Hashtable<String, ArrayList<ArrayList<Integer>>> readMessages(JsonReader reader) throws IOException {
        Hashtable<String, ArrayList<ArrayList<Integer>>> messages = new Hashtable<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            messages.put(name,readMatrix(reader));
        }
        reader.endObject();
        return messages;
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
