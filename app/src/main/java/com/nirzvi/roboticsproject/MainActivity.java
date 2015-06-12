package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.ImageAnalysis;
import com.nirzvi.roboticslibrary.MyCamera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextureView texture;
    TextView colour;
    TextView rgb;
    TextView rgbval;
    ImageView img2;
    ImageView img;
    MyCamera cam;
    int runCount = 0;
    int memorySave = 10;
    ImageAnalysis imgA = new ImageAnalysis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texture = (TextureView) findViewById(R.id.textureView);
        cam = new MyCamera(texture, true, this) {
            public void onCameraUpdated() {
                runCount++;

                if (runCount % memorySave == 0)
                    takeImage();
            }
        };
        rgb = (TextView) findViewById(R.id.text);
        colour = (TextView) findViewById(R.id.colour);
        img = (ImageView) findViewById(R.id.img);

    }

    public void takeImage () {

        Bitmap bm = texture.getBitmap();

        averageColour(bm);

        imgA.accuracy = imgA.DETAILS;

        img.setImageBitmap(imgA.getEdges(bm));

    }

    public int averageColour(Bitmap bit) {
        long greenF = 0;
        long redF = 0;
        long blueF = 0;
        long overallColour = 0;
        int numPixels = 0;
        int[] pixels = new int [bit.getWidth() * bit.getHeight()];

        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            redF += Color.red(pixels[i]);
            blueF += Color.blue(pixels[i]);
            greenF += Color.green(pixels[i]);
            numPixels++;
        }

        redF /= numPixels;
        greenF /= numPixels;
        blueF /= numPixels;

        colour.setBackgroundColor(Color.rgb((int) redF, (int) greenF, (int) blueF));

        return Color.rgb((int) redF, (int) greenF, (int) blueF);
    }



}