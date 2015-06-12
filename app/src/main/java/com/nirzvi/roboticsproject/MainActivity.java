package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.ImageAnalysis;
import com.nirzvi.roboticslibrary.MyCamera;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextureView texture;
    TextView colour;
    TextView rgb;
    ImageView img;
    MyCamera cam;
    int runCount = 0;
    ImageAnalysis imgA = new ImageAnalysis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texture = (TextureView) findViewById(R.id.textureView);
        cam = new MyCamera(texture, true, this) {
            public void onCameraUpdated() {
                runCount++;

                if (runCount % 10 == 0)
                    takeImage();
            }
        };
        rgb = (TextView) findViewById(R.id.text);
        colour = (TextView) findViewById(R.id.colour);
        img = (ImageView) findViewById(R.id.img);

    }

    public void takeImage () {

        imgA.accuracy = imgA.DETAILS;

        img.setImageBitmap(imgA.getEdges(texture.getBitmap()));

    }

}