package com.nirzvi.roboticslibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Nirzvi on 2015-06-11.
 */
public class ImageAnalysis {

    public final int OUTLINES = 650000;
    public final int DETAILS = 300000;
    public int accuracy = DETAILS;

    public Bitmap getEdges (Bitmap bit) {

        int[] pixels = new int[bit.getWidth() * bit.getHeight()];
        Bitmap newBit = bit.createBitmap(bit.getWidth(), bit.getHeight(), Bitmap.Config.ARGB_8888);
        long colour = 0;
        int recordPixels = 1;
        int x = 0;
        int y = 0;
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);

        Canvas can = new Canvas(newBit);
        Paint colourPaint = new Paint();

        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());

        colourPaint.setColor(Color.WHITE);
        can.drawRect(0, 0, bit.getWidth(), bit.getHeight(), colourPaint);
        colourPaint.setColor(Color.BLACK);

        for (int i = 1; i < pixels.length; i++) {
            if (Math.abs(pixels[i] - pixels[i - 1]) > accuracy) {
                x = i % bit.getWidth();
                y = i / bit.getWidth();
                can.drawLine(x, y, x + 1, y, colourPaint);
            } else {
                recordPixels++;
            }
        }

        return Bitmap.createBitmap(newBit, 0, 0, newBit.getWidth(), newBit.getHeight(), matrix, true);
    }

}
