package com.nirzvi.roboticslibrary;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.os.PowerManager;
import android.view.TextureView;

/**
 * Created by Nirzvi on 2015-05-22.
 */
public class MyCamera {

    Context c;
    boolean powerSave;
    Camera cam;
    boolean on = true;
    boolean gone = false;
    boolean onPause = false;

    PowerManager pm;

    final Runnable r = new Runnable() {
        public void run() {
            while (!gone) {
                if (!pm.isScreenOn() && on && !gone) {
                    try {
                        cam.stopPreview();
                        on = false;
                    } catch (Exception e) {

                    }

                } else if (pm.isScreenOn() && !on && !gone && !onPause) {
                    try {
                        cam.startPreview();
                        on = true;
                    } catch (Exception e) {

                    }

                }
                try {
                    Thread.sleep(10);
                } catch (Exception e) {

                }
            }
        }
    };

    Thread powerCheck;

    Handler handler;



    public MyCamera (TextureView name, boolean power, Context cTemp) {

        powerSave = power;
        c = cTemp;
        powerCheck = new Thread(r);

        pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        handler = new Handler();

        TextureView.SurfaceTextureListener stl = new TextureView.SurfaceTextureListener() {

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                handler.removeCallbacks(r);
                gone = true;

                if (on) {
                    cam.stopPreview();
                    on = false;
                }


                cam.release();

                return false;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

                cam = Camera.open();

                try {
                    cam.setPreviewTexture(surface);
                    cam.startPreview();
                    on = true;
                    if (powerSave) {
                        powerCheck.start();
                    }
                } catch (Exception yay) {

                }

            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }

        };

        name.setSurfaceTextureListener(stl);
    }

    public void resume() {

        try {
            cam.startPreview();
            on = true;
            onPause = false;
        } catch (Exception e) {
        }

    }

    public void pause() {

        try {
            cam.stopPreview();
            on = false;
            onPause = true;
        } catch (Exception e) {
        }

    }

    public void dispose() {
        handler.removeCallbacks(r);
        gone = true;

        if (on) {
            cam.stopPreview();
            on = false;
        }

        cam.release();

    }

}
