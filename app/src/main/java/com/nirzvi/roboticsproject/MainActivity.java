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

        img.setImageBitmap(imgA.findBlobs(bm));
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


}