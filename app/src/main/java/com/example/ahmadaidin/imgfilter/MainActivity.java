package com.example.ahmadaidin.imgfilter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    BitmapEditor bitmapEditor;
    ImageView imgView;
    Bitmap bitmap;
    Convolution cvl;
    String s = "";
    OtsuConverter otsu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream inputStream;
        Resources resource = getResources();
        inputStream = resource.openRawResource(R.raw.matrix);
        cvl = new Convolution(inputStream);
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
                otsu.countThreshold(bitmapEditor.getGrayscale(),bitmapEditor.getGrayHistogram());
            } else {
                Toast.makeText(this, "you haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void resetImage(View view) {
        if (bitmapEditor.bitmap()!= null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.resetBitmap();
            imgView.setImageBitmap(bitmapEditor.bitmap());
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

    public void blurImage(View view) {
        if(bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.blurImage();
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }

    public void detectEdge1(View view) {
        if(bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.detectEdge1();
            bitmapEditor.binaryConvert(otsu);
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }

    public void detectEdge2(View view) {
        if(bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.detectEdge2();
            bitmapEditor.binaryConvert(otsu);
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }

    public void robert(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.robert(cvl);
            bitmapEditor.binaryConvert(otsu);
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void prewit(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv1(cvl,"Prewit");
            bitmapEditor.binaryConvert(otsu);
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void sobel(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv1(cvl,"Sobel");
            bitmapEditor.binaryConvert(otsu);
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void freiChi(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv1(cvl,"Frei-Chi");
            bitmapEditor.binaryConvert(otsu);
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void prewit8(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv2(cvl,"Prewit8");
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void kirsch(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv2(cvl,"Kirsch");
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void robinson3(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv2(cvl,"Robinson3");
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
    public void robinson5(View view) {
        if (bitmapEditor.bitmap() != null) {
            imgView = (ImageView) findViewById(R.id.imgView);
            bitmapEditor.edgeDetectLv2(cvl,"Robinson5");
            imgView.setImageBitmap(bitmapEditor.bitmap());
        }
    }
}
