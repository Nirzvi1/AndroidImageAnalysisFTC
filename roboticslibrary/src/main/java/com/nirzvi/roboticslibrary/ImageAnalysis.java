package com.nirzvi.roboticslibrary;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Nirzvi on 2015-06-11.
 */
public class ImageAnalysis {

    public double blobAccuracy = 2.2;
    public double edgeAccuracy = 500000;

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
        int newCoord;

        Canvas can = new Canvas(newBit);
        Paint colourPaint = new Paint();

        colourPaint.setColor(Color.WHITE);
        can.drawRect(0, 0, newBit.getWidth(), newBit.getHeight(), colourPaint);
        colourPaint.setColor(Color.BLACK);

        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());

        for (int i = 1; i < pixels.length; i++) {
            if (closeToColour(pixels[i]) != closeToColour(pixels[i - 1])) {
                x = i % bit.getWidth();
                y = i / bit.getWidth();
                can.drawRect(x, y, x + 1, y + 1, colourPaint);
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
            if (closeToColour(pixels[i]) != closeToColour(pixels[i - 1])) {
                y = i % bitWidth;
                x = i / bitWidth;
                can.drawLine(x, y, x + 1, y + 1, colourPaint);
            }
        }

        return newBit;

    }

    public int closeToColour (int colour) {

        int closestColour;

        //Log.i("First Colour", "" + colour);

        closestColour = Color.argb(255, Color.red(colour) - (int) (Color.red(colour) % (255 / blobAccuracy)),
                Color.green(colour) - (int) (Color.green(colour) % (255 / blobAccuracy)),
                Color.blue(colour) - (int) (Color.blue(colour) % (255 / blobAccuracy)));

        return closestColour;
    }

    public Bitmap getAmbientEdges (Bitmap bit) {

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
        int newCoord;

        Canvas can = new Canvas(newBit);
        Paint colourPaint = new Paint();

        colourPaint.setColor(Color.WHITE);
        can.drawRect(0, 0, newBit.getWidth(), newBit.getHeight(), colourPaint);
        colourPaint.setColor(Color.BLACK);

        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());

        for (int i = 1; i < pixels.length; i++) {
            if (Math.abs(pixels[i] - pixels[i - 1]) > edgeAccuracy) {
                x = i % bit.getWidth();
                y = i / bit.getWidth();
                can.drawRect(x, y, x + 1, y + 1, colourPaint);
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
            if (Math.abs(pixels[i] - pixels[i - 1]) > edgeAccuracy) {
                y = i % bitWidth;
                x = i / bitWidth;
                can.drawLine(x, y, x + 1, y + 1, colourPaint);
            }
        }

        return newBit;

    }

    public int[] findBlobLabels (Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        int check = 1;
        int x = 0;
        int y = 0;
        int [] pixels = new int[width * height];
        int [] label = new int [width * height];

        img = getAmbientEdges(img);

        img.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i = 0; i < label.length; i++){
            label[i] = -1;
        }//for

        label[0] = 0;

        for(int i = 0; i < pixels.length - width - 1; i++){
            int same = -1;
            if(i % width == width - 1){
                i++;
                continue;
            }

            //check north
            if(pixels[i] == pixels[i + width]){
                label[i + width] = label[i];
                same = 2;
            }
            //check west
            if(pixels[i] == pixels[i + 1]){
                label[i + 1] = label[i];
                same = 1;
            }

            //check northwest
            if(pixels[i] == pixels[i + width + 1] && same != -1){
                label[i + width + 1] = label[i];
            }

            if(pixels[i] == Color.BLACK) {
                if (pixels[i + 1] == pixels[i + width] && pixels[i + 1] == pixels[i + width + 1]) {
                    if (label[i + width] != -1) {
                        label[i + 1] = label[i + width];
                        label[i + width + 1] = label[i + width];
                    } else if (label[i + 1] != -1) {
                        label[i + width] = label[i + 1];
                        label[i + width + 1] = label[i + 1];
                    } else {
                        label[i + 1] = check;
                        label[i + width] = check;
                        label[i + width + 1] = check;
                        check++;
                    }
                } else if (pixels[i + 1] == pixels[i + width]) {
                    if (label[i + 1] != -1) {
                        label[i + width] = label[i + 1];
                    } else if (label[i + width] != -1) {
                        label[i + 1] = label[i + width];
                    } else {
                        label[i + width] = check;
                        label[i + 1] = check;
                        check++;
                    }
                } else if (pixels[i + 1] == pixels[i + width + 1]) {
                    if (label[i + 1] != -1) {
                        label[i + width + 1] = label[i + 1];
                    } else {
                        label[i + 1] = check;
                        label[i + width + 1] = check;
                        check++;
                    }
                } else if (pixels[i + width] == pixels[i + width + 1]) {
                    if (label[i + width] != -1) {
                        label[i + width + 1] = label[i + width];
                    } else {
                        label[i + width] = check;
                        label[i + width + 1] = check;
                        check++;
                    }
                }
            }
        }

        for(int i = pixels.length - 1; i >= width + 1; i--) {
            if(label[i] != label[i - 1] && pixels[i] == pixels[i - 1]){
                replace(label, label[i - 1], label[i]);
            }
            if(label[i] != label[i - width] && pixels[i] == pixels[i - width]){
                replace(label, label[i - width], label[i]);
            }
        }

        return label;
    }

    public Bitmap findBlobs(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        int check = 1;
        int x = 0;
        int y = 0;
        int [] pixels = new int[width * height];
        int [] label = new int [width * height];

        img = getAmbientEdges(img);

        img.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i = 0; i < label.length; i++){
            label[i] = -1;
        }//for

        label[0] = 0;

        for(int i = 0; i < pixels.length - width - 1; i++){
            int same = -1;
            if(i % width == width - 1){
                i++;
                continue;
            }

            //check north
            if(pixels[i] == pixels[i + width]){
                label[i + width] = label[i];
                same = 2;
            }
            //check west
            if(pixels[i] == pixels[i + 1]){
                label[i + 1] = label[i];
                same = 1;
            }

            //check northwest
            if(pixels[i] == pixels[i + width + 1] && same != -1){
                label[i + width + 1] = label[i];
            }

            if(pixels[i] == Color.BLACK) {
                if (pixels[i + 1] == pixels[i + width] && pixels[i + 1] == pixels[i + width + 1]) {
                    if (label[i + width] != -1) {
                        label[i + 1] = label[i + width];
                        label[i + width + 1] = label[i + width];
                    } else if (label[i + 1] != -1) {
                        label[i + width] = label[i + 1];
                        label[i + width + 1] = label[i + 1];
                    } else {
                        label[i + 1] = check;
                        label[i + width] = check;
                        label[i + width + 1] = check;
                        check++;
                    }
                } else if (pixels[i + 1] == pixels[i + width]) {
                    if (label[i + 1] != -1) {
                        label[i + width] = label[i + 1];
                    } else if (label[i + width] != -1) {
                        label[i + 1] = label[i + width];
                    } else {
                        label[i + width] = check;
                        label[i + 1] = check;
                        check++;
                    }
                } else if (pixels[i + 1] == pixels[i + width + 1]) {
                    if (label[i + 1] != -1) {
                        label[i + width + 1] = label[i + 1];
                    } else {
                        label[i + 1] = check;
                        label[i + width + 1] = check;
                        check++;
                    }
                } else if (pixels[i + width] == pixels[i + width + 1]) {
                    if (label[i + width] != -1) {
                        label[i + width + 1] = label[i + width];
                    } else {
                        label[i + width] = check;
                        label[i + width + 1] = check;
                        check++;
                    }
                }
            }
        }

        for(int i = pixels.length - 1; i >= width + 1; i--) {
            if(label[i] != label[i - 1] && pixels[i] == pixels[i - 1]){
                replace(label, label[i - 1], label[i]);
            }
            if(label[i] != label[i - width] && pixels[i] == pixels[i - width]){
                replace(label, label[i - width], label[i]);
            }
        }

        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        for(int i = 0; i < pixels.length; i++) {
            x = i % width;
            y = i / width;
            color.setColor(label[i] * -4500 - 1);
            pic.drawRect(x, y, x + 2, y + 2, color);
        }//for


        bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        pic = new Canvas(bitsy);
        for(int i = 0; i < pixels.length; i++) {
            x = i % width;
            y = i / width;
            color.setColor(label[i] * -4500 - 1);
            pic.drawRect(x, y, x + 2, y + 2, color);
        }//for

        shapeDetector(label, width);

        return bitsy;
    }

    public void replace(int [] label, int l1, int l2){
        for(int i  = 0; i < label.length; i++){
            if(label[i] == l1){
                label[i] = l2;
            }
        }
    }

    public int[] shapeDetector (int[] labels, int imageWidth) {

        List<Integer> colours = new ArrayList<>();
        List<Integer> colourRanks = new ArrayList<>();
        List<List<Integer>> colourCoords = new ArrayList<>();
        int[] blobRanks;
        boolean highRank;
        int index;
        int[] centers = new int[5];

        for (int i = 0; i < labels.length; i++) {

            if (labels[i] != -1)
                if (!colours.contains(labels[i])) {

                    colourRanks.add(new Integer(0));
                    colours.add(labels[i]);
                    colourCoords.add(new ArrayList<Integer>());
                    colourCoords.get(colourCoords.size() - 1).add(i);

                } else {

                    colourRanks.set(colours.indexOf(labels[i]), new Integer(colourRanks.get(colours.indexOf(labels[i])) + 1));
                    colourCoords.get(colours.indexOf(labels[i])).add(i);

                }

        }

        blobRanks = new int[5];

        for (int i = 0; i < colours.size(); i++) {

            index = i;
            highRank = false;

            if (i > 4)
                for (int j = 0; j < 5; j++) {

                    if (colourRanks.get(index) > colourRanks.get(blobRanks[j])) {
                        highRank = true;
                        index = j;
                    }

                }
            else
                highRank = true;

            if (highRank) {
                blobRanks[index] = i;
            }

        }



        for (int i = 0; i < blobRanks.length; i++) {

            centers[i] = getCenter(colourCoords.get(blobRanks[i]), imageWidth);
            blobRanks[i] = colours.get(blobRanks[i]);
            Log.i("Colours", " Colour: " + (blobRanks[i] * -4500 - 1) + "Center: " + centers[i]);

        }

        return blobRanks;

    }

    public int getCenter (List<Integer> pixels, int imageWidth) {

        int x = 0;
        int y = 0;

        for (int i = 0; i < pixels.size(); i++) {

            x += pixels.get(i) % imageWidth;
            y += pixels.get(i) / imageWidth;

        }

        x /= pixels.size();
        y /= pixels.size();

        return (x + (y * imageWidth));

    }

}