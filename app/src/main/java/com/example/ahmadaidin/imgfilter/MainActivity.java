package com.example.ahmadaidin.imgfilter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    BitmapEditor bitmapEditor;
    ImageView imgView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void loadImagefromGallery(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                bitmap = BitmapFactory.decodeFile(imgDecodableString,opt);
                imgView = (ImageView) findViewById(R.id.imgView);
                imgView.setImageBitmap(bitmap);
                bitmapEditor = new BitmapEditor(bitmap);
            } else {
                Toast.makeText(this, "you haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void filterImage(View view) {
        if (bitmapEditor.bitmap()!= null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.grayLvHistogramEqualization();
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }

    public void smoothImage(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.smoothImage();
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }

    public void sharpImage(View view){
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.sharpImage();
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }

}
