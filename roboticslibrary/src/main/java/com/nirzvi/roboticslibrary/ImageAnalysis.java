package com.nirzvi.roboticslibrary;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

/**
 * Created by Nirzvi on 2015-06-11.
 */
public class ImageAnalysis {

    public final int OUTLINES = 1300000;
    public final int DETAILS = 300000;
    public int accuracy = DETAILS;

    public Bitmap getEdges (Bitmap bit) {

        int bitWidth = bit.getWidth();
        int bitHeight = bit.getHeight();
        int[] pixels = new int[bit.getWidth() * bit.getHeight()];
        int[] storePixels = new int[pixels.length];
        Bitmap newBit;
        int difference = Math.abs(bit.getWidth() - bit.getHeight());
        bitWidth -= difference;

        newBit = bit.createBitmap(bit.getWidth(), bit.getHeight(), Bitmap.Config.ARGB_8888);

        int x;
        int y;
        int newCoord = 0;

        Canvas can = new Canvas(newBit);
        Paint colourPaint = new Paint();

        //bit.getPixels(pixels, 0, bit.getWidth(), (difference / 2), 0, bit.getWidth() - difference, bit.getHeight());
        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());
        colourPaint.setColor(Color.WHITE);
        //can.drawRect(0, 0, bit.getWidth() - difference, bit.getHeight(), colourPaint);
        can.drawRect(0, 0, bit.getWidth(), bit.getHeight(), colourPaint);
        colourPaint.setColor(Color.BLACK);

        for (int i = 1; i < pixels.length; i++) {
            if (Math.abs(pixels[i] - pixels[i - 1]) > accuracy) {
                x = i % bit.getWidth();
                y = i / bit.getWidth();
                can.drawLine(x, y, x + 1, y, colourPaint);
            }
        }

        for (int i = 0; i < pixels.length; i++) {
            storePixels[i] = pixels[i];
        }

        for (int i = 1; i < pixels.length; i++) {

            if (!((i % bitWidth) >= bitHeight))
                newCoord = (i % bitWidth * bit.getWidth()) + (i / bitWidth);
            else
                newCoord = 0;

            pixels[i] = storePixels[newCoord];
        }

        for (int i = 1; i < pixels.length; i++) {
            if (Math.abs(pixels[i] - pixels[i - 1]) > accuracy) {
                y = i % bitWidth;
                x = i / bitWidth;
                can.drawLine(x, y, x + 1, y, colourPaint);
            }
        }

        return newBit;
    }

    public int averageColour(Bitmap bit, int startX, int startY, int endX, int endY) {
        long greenF = 0;
        long redF = 0;
        long blueF = 0;
        int numPixels = 0;
        int[] pixels = new int [bit.getWidth() * bit.getHeight()];

        bit.getPixels(pixels, 0, bit.getWidth(), startX, startY, endX, endY);

        for (int i = 0; i < pixels.length; i++) {
            redF += Color.red(pixels[i]);
            blueF += Color.blue(pixels[i]);
            greenF += Color.green(pixels[i]);
            numPixels++;
        }

        redF /= numPixels;
        greenF /= numPixels;
        blueF /= numPixels;

        return Color.rgb((int) redF, (int) greenF, (int) blueF);
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

        return Color.rgb((int) redF, (int) greenF, (int) blueF);
    }

}
