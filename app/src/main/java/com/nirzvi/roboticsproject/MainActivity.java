package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.ImageAnalysis;
import com.nirzvi.roboticslibrary.MyCamera;
import com.nirzvi.roboticslibrary.MySensorManager;
import com.nirzvi.roboticsproject.AlecImage;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;


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
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextureView texture;
    TextView colour;
    TextView rgb;
    ImageView img;
    ImageAnalysis imgA = new ImageAnalysis();
    MyCamera cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texture = (TextureView) findViewById(R.id.textureView);
        cam = new MyCamera(texture, true, this);
        rgb = (TextView) findViewById(R.id.text);
        colour = (TextView) findViewById(R.id.colour);
        img = (ImageView) findViewById(R.id.img);

    }

    public void takeImage (View v) {

        Bitmap bm = texture.getBitmap();

        img.setImageBitmap(imgA.findBlobCircle(bm));
    }


    public void takeEdgeImage (View v) {

        Bitmap bm = texture.getBitmap();

        img.setImageBitmap(imgA.getEdges(bm));
    }


    public void getClosest (View v) {

        Bitmap bm = texture.getBitmap();

        img.setImageBitmap(imgA.getAmbientEdges(bm));
    }

    public void incAcc (View v) {
        imgA.blobAccuracy /= 1.1;
        imgA.edgeAccuracy -= 50000;
    }

    public void decAcc (View v) {
        imgA.blobAccuracy *= 1.1;
        imgA.edgeAccuracy += 50000;
    }

    public int averageColour(Bitmap bit) {
        long greenF = 0;
        long redF = 0;
        System.out.println(colour);
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

    public int closeToColour (int colour) {

        int closestColour;
        int difference;

        difference = Math.abs(Color.BLACK - colour);
        closestColour = Color.BLACK;

        if (Math.abs(Color.BLUE - colour) < difference) {
            closestColour = Color.BLUE;
            difference = Math.abs(Color.BLUE - colour);
        }

        if (Math.abs(Color.RED - colour) < difference) {
            closestColour = Color.RED;
            difference = Math.abs(Color.RED - colour);
        }

        if (Math.abs(Color.DKGRAY - colour) < difference) {
            closestColour = Color.DKGRAY;
            difference = Math.abs(Color.DKGRAY - colour);
        }

        if (Math.abs(Color.YELLOW - colour) < difference) {
            closestColour = Color.YELLOW;
            difference = Math.abs(Color.YELLOW - colour);
        }

        if (Math.abs(Color.LTGRAY - colour) < difference) {
            closestColour = Color.LTGRAY;
            difference = Math.abs(Color.LTGRAY - colour);
        }

        if (Math.abs(Color.MAGENTA - colour) < difference) {
            closestColour = Color.MAGENTA;
            difference = Math.abs(Color.MAGENTA - colour);
        }

        if (Math.abs(Color.WHITE - colour) < difference) {
            closestColour = Color.WHITE;
            difference = Math.abs(Color.WHITE - colour);
        }

        if (Math.abs(Color.CYAN - colour) < difference) {
            closestColour = Color.CYAN;
            difference = Math.abs(Color.CYAN - colour);
        }

        if (Math.abs(Color.GREEN - colour) < difference) {
            closestColour = Color.GREEN;
            difference = Math.abs(Color.GREEN - colour);
        }

        return closestColour;
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