package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.CannyEdgeDetector;
import com.nirzvi.roboticslibrary.ImageAnalysis;
import com.nirzvi.roboticslibrary.MyCamera;
import com.nirzvi.roboticslibrary.MySensorManager;

import android.graphics.Bitmap;
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
    CannyEdgeDetector canEdge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rgb = (TextView) findViewById(R.id.text);
        colour = (TextView) findViewById(R.id.colour);
        img = (ImageView) findViewById(R.id.img);
        texture = (TextureView) findViewById(R.id.textureView);
        canEdge = new CannyEdgeDetector();

        BitmapFactory.Options d = new BitmapFactory.Options();
        d.inScaled = false;
        Bitmap colours = BitmapFactory.decodeResource(getResources(),R.drawable.chrome5, d);
        imgA.chrome = colours;

        cam = new MyCamera(texture, true, this) {
            public void onCameraUpdated() {

//                img.setImageBitmap(imgA.blurMethod(imgA.chrome(texture.getBitmap()), true));

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

        img.setImageBitmap(imgA.blurMethod(imgA.chrome(texture.getBitmap()), true));

    }


    public void takeEdgeImage (View v) {
        img.setImageBitmap(imgA.blurMethod(imgA.chrome(texture.getBitmap()), false));

    }


    public void getClosest (View v) {

        img.setImageBitmap(imgA.chrome(texture.getBitmap()));

//        canEdge.setSourceImage(texture.getBitmap());
//        canEdge.setHighThreshold(1f);
//        canEdge.setLowThreshold(0.1f);
//        canEdge.process();
//        img.setImageBitmap(canEdge.getEdgesImage());

    }

    public void incAcc (View v) {
        imgA.decreaseBlur();
    }

    public void decAcc (View v) {
        imgA.increaseBlur();
    }
}