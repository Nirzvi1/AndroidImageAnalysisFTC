package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.ImageAnalysis;
import com.nirzvi.roboticslibrary.MyCamera;
import com.nirzvi.roboticsproject.AlecImage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextureView texture;
    TextView colour;
    TextView rgb;
    ImageView img;
    ImageAnalysis imgA = new ImageAnalysis();
    AlecImage al;
    MyCamera cam;
    int runCount = 0;
    final int OUTLINES = 650000;
    final int DETAILS = 300000;
    int accuracy = DETAILS;
    int memorySave = 10;


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
        al = new AlecImage();

    }

    public void takeImage () {

        Bitmap bm = texture.getBitmap();

        averageColour(bm);
        img.setImageBitmap(imgA.getEdges(bm));
    }

    public int averageColour(Bitmap bit) {
        long greenF = 0;
        long redF = 0;
        long blueF = 0;
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

    public String closeToColour (long colour) {

        String closestColour;
        long difference;

        difference = Math.abs(Color.BLACK - colour);
        closestColour = "black";

        if (Math.abs(Color.BLUE - colour) < difference) {
            closestColour = "blue";
            difference = Math.abs(Color.BLUE - colour);
        }

        if (Math.abs(Color.RED - colour) < difference) {
            closestColour = "red";
            difference = Math.abs(Color.RED - colour);
        }

        if (Math.abs(Color.DKGRAY - colour) < difference) {
            closestColour = "dark grey";
            difference = Math.abs(Color.DKGRAY - colour);
        }

        if (Math.abs(Color.YELLOW - colour) < difference) {
            closestColour = "yellow";
            difference = Math.abs(Color.YELLOW - colour);
        }

        if (Math.abs(Color.LTGRAY - colour) < difference) {
            closestColour = "light grey";
            difference = Math.abs(Color.LTGRAY - colour);
        }

        if (Math.abs(Color.MAGENTA - colour) < difference) {
            closestColour = "magenta";
            difference = Math.abs(Color.MAGENTA - colour);
        }

        if (Math.abs(Color.WHITE - colour) < difference) {
            closestColour = "white";
            difference = Math.abs(Color.WHITE - colour);
        }

        if (Math.abs(Color.CYAN - colour) < difference) {
            closestColour = "cyan";
            difference = Math.abs(Color.CYAN - colour);
        }

        if (Math.abs(Color.GREEN - colour) < difference) {
            closestColour = "green";
            difference = Math.abs(Color.GREEN - colour);
        }

        return closestColour;
    }



}