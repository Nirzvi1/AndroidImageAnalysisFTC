package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.ImageAnalysis;
import com.nirzvi.roboticslibrary.MyCamera;
import com.nirzvi.roboticslibrary.MySensorManager;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    RelativeLayout rl;
    TextView colour;
    TextView rgb;
    TextureView texture;
    ImageView img;
    ImageAnalysis imgA = new ImageAnalysis();
    float angle = 0;
    MySensorManager sm;
    MyCamera cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rgb = (TextView) findViewById(R.id.text);
        colour = (TextView) findViewById(R.id.colour);
        img = (ImageView) findViewById(R.id.img);
        texture = (TextureView) findViewById(R.id.textureView);
        cam = new MyCamera(texture, true, this) {
            public void onCameraUpdated() {

                //noButtonImage();

            }
        };
        sm = new MySensorManager(this);
        sm.addSensor(sm.ACCEL, "accel");
        sm.addSensor(sm.MAG, "mag");
        rl = (RelativeLayout) findViewById(R.id.bg);

    }

    public void noButtonImage () {

        img.setImageBitmap(imgA.multiCircle(imgA.getAmbientEdges(texture.getBitmap())));

    }

    public void takeImage (View v) {

        img.setImageBitmap(imgA.multiCircle(imgA.getAmbientEdges(BitmapFactory.decodeResource(getResources(), R.drawable.unnamed))));

    }


    public void takeEdgeImage (View v) {

        img.setImageBitmap(imgA.multiCircle(imgA.getAmbientEdges(texture.getBitmap())));
        //img.setImageBitmap(imgA.multiCircle(texture.getBitmap()));

    }


    public void getClosest (View v) {
        img.setImageBitmap(imgA.getAmbientEdges(texture.getBitmap()));

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