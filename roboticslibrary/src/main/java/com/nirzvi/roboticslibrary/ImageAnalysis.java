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

    public double edgeAccuracy = 10;
    public int rayIntensity = 50;
    public int numCentrePoints = 9;
    public int borderLimit = 1;
    public int switchLimit = 1;

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

        int closestColour = Color.argb(255, Color.red(colour) - (int) (Color.red(colour) % (255 / blobAccuracy)),
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
            if ((i % bit.getWidth() > 0 && (Math.abs(Color.red(pixels[i - 1]) - Color.red(pixels[i])) > edgeAccuracy
                || Math.abs(Color.green(pixels[i - 1]) - Color.green(pixels[i])) > edgeAccuracy
                || Math.abs(Color.blue(pixels[i - 1]) - Color.blue(pixels[i])) > edgeAccuracy))) {
                x = i % bit.getWidth();
                y = i / bit.getWidth();
                can.drawRect(x, y, x + 1, y + 1, colourPaint);
            } else if (i % bit.getWidth() == 0 || i % bit.getWidth() == bit.getWidth() - 1
                    || i / bit.getWidth() == 0 || i / bit.getWidth() == bit.getHeight() - 1) {
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
            if ((i % bit.getWidth() > 0 && (Math.abs(Color.red(pixels[i - 1]) - Color.red(pixels[i])) > edgeAccuracy
                    || Math.abs(Color.green(pixels[i - 1]) - Color.green(pixels[i])) > edgeAccuracy
                    || Math.abs(Color.blue(pixels[i - 1]) - Color.blue(pixels[i])) > edgeAccuracy))) {
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

        Arrays.fill(label, -1);

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

        Arrays.fill(label, -1);

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

        //shapeProcessor(label, width);

        return bitsy;
    }

    public void replace(int [] label, int l1, int l2){
        for(int i  = 0; i < label.length; i++){
            if(label[i] == l1){
                label[i] = l2;
            }
        }
    }




















    public Bitmap speedBlobs (Bitmap bit) {

        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        int[] pixels = new int[imageWidth * imageHeight];
        int[] labels = new int[pixels.length];
        getAmbientEdges(bit).getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
        List<List<Integer>> labelTypes = new ArrayList<>();
        boolean connect = false;
        int newLabel = 1;

        labels[0] = 1;

        labelTypes.add(new ArrayList<Integer>());

        for (int i = 1; i < pixels.length; i++) {

            if (i % imageWidth > 0 && pixels[i - 1] != Color.BLACK && pixels[i] == pixels[i - 1]) {
                labels[i] = labels[i - 1];
                labelTypes.get(labels[i] - 1).add(i);
            } else if (i / imageWidth > 0 && pixels[i - imageWidth] != Color.BLACK && pixels[i] == pixels[i - imageWidth]) {
                labels[i] = labels[i - imageWidth];
                labelTypes.get(labels[i] - 1).add(i);
            } else {
                labels[i] = ++newLabel;
                labelTypes.add(new ArrayList<Integer>());
                labelTypes.get(newLabel - 1).add(i);
            }

        }

        for (int i = 0; i < labels.length; i++) {

            if (i / imageWidth > 0 && pixels[i - imageWidth] != Color.BLACK
                    && pixels[i] == pixels[i - imageWidth] && labels[i] != labels[i - imageWidth]) {

            }

        }

        for (int i = 0; i < pixels.length; i++) {

            pixels[i] = (labels[i] * -4500 - 1);

        }

        bit = Bitmap.createBitmap(pixels, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;
    }

    public int[] shapeProcessor (int[] labels, int imageWidth) {

        List<Integer> colours = new ArrayList<>();
        List<Integer> colourRanks = new ArrayList<>();
        List<List<Integer>> colourCoords = new ArrayList<>();
        int[] blobRanks;
        boolean highRank;
        int index;
        int[] centres = new int[5];

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

            centres[i] = getCentre(colourCoords.get(blobRanks[i]), imageWidth);
            blobRanks[i] = colours.get(blobRanks[i]);
            Log.i("Colours", " Colour: " + (blobRanks[i] * -4500 - 1) + "Centre: " + centres[i]);

        }

        return blobRanks;

    }

    public int getCentre (List<Integer> pixels, int imageWidth) {

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

    public Bitmap circleEdges (Bitmap bit) {
        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        int[] pixels = new int[imageHeight * imageWidth];
        int[] pixStore = new int[pixels.length];
        int[] newPixels = new int[pixels.length];
        int[] newEdgePixels = new int[pixels.length];
        int[] pixRadii = new int[pixels.length];
        int[] indexes = new int[pixels.length];
        int[] label = new int[pixels.length];
        int radianCount = 0;
        int radius = 1;
        int x;
        int y;
        double tempX;
        double tempY;
        int centre = pixels.length / 2 + (imageWidth / 2);
        double radians;
        boolean doNotUse;
        int newLabel = 1;
        double incRad = Math.PI / (2 * rayIntensity);
        int[][] allRadii = new int[1000][1000];
        int[] pixRadCounts = new int[pixels.length];

        bit.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

//        for (int i = 0; i < newPixels.length; i++) {
//            pixStore[i] = -1;
//        }

        newPixels[0] = pixels[centre];

        radians = incRad;

        tempX = Math.cos(radians);
        tempY = Math.sin(radians);

        for (int i = 1; i < pixels.length; i++) {

            x = (int) (tempX * radius);
            y = (int) (tempY * radius);

            pixRadii[i] = radius;

            radius++;

            doNotUse = false;
            if ((centre % imageWidth) + x < 0 || (centre % imageWidth) + x > imageWidth - 1)
                doNotUse = true;
            else if ((centre / imageWidth) + y < 0 || (centre / imageWidth) + y > imageHeight - 1)
                doNotUse = true;

            if (!doNotUse) {
                newPixels[i] = pixels[centre + x + (y * imageWidth)];
                allRadii[radius][radianCount] = i;
                pixRadCounts[i] = radianCount;
            } else {
                radianCount++;
                radians += incRad;

                radius = 1;

                tempX = Math.cos(radians);
                tempY = Math.sin(radians);

            }

        }

        for (int i = 1; i < newPixels.length; i++) {
            if (Math.abs(newPixels[i] - newPixels[i - 1]) > edgeAccuracy) {
                newEdgePixels[i] = Color.BLACK;
            } else {
                newEdgePixels[i] = Color.WHITE;
            }
        }

        label[0] = 1;

        Arrays.fill(pixStore, -1);

        pixStore[centre] = newPixels[0];

        radians = incRad;

        radius = 1;

        tempX = Math.cos(radians);
        tempY = Math.sin(radians);

        for (int i = 1; i < pixels.length; i++) {

            x = (int) (tempX * radius);
            y = (int) (tempY * radius);

            radius++;

            doNotUse = false;
            if ((centre % imageWidth) + x < 0 || (centre % imageWidth) + x > imageWidth - 1)
                doNotUse = true;
            else if ((centre / imageWidth) + y < 0 || (centre / imageWidth) + y > imageHeight - 1)
                doNotUse = true;

            if (!doNotUse) {
                pixStore[centre + x + (y * imageWidth)] = newEdgePixels[i];
            } else {
                radians += incRad;

                radius = 1;

                tempX = Math.cos(radians);
                tempY = Math.sin(radians);

            }

        }


        bit = Bitmap.createBitmap(pixStore, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;


    }

    public Bitmap blobsCircle (Bitmap bit) {
        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        int[] pixels = new int[imageHeight * imageWidth];
        int[] switchPixStore = new int[pixels.length];
        int[] switchLabelStore = new int[pixels.length];
        int[] pixStore = new int[pixels.length];
        int[] newPixels = new int[pixels.length];
        int radius = 0;
        int x;
        int y;
        int centre = pixels.length / 2 + 3 * (imageWidth / 4);
        double radians = 0;
        int stageInCycle = 0;
        int cycleNum = 1;
        boolean doNotUse = false;
        int newLabel = 1;
        int borderCount = 0;
        int[] label = new int[pixels.length];
        int[] pixRadii = new int[pixels.length];
        int[] centrePoints = {imageWidth / 4 + (imageWidth * imageHeight / 4), 3 * imageWidth / 4 + (imageWidth * imageHeight / 4),
                imageWidth / 4 + (3 * imageWidth * imageHeight / 4),  3 * imageWidth / 4 + (3 * imageWidth * imageHeight / 4)};

        bit.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        for (int i = 0; i < newPixels.length; i++) {
            pixStore[i] = Color.BLACK;
        }

        for (int j = 0; j < 4; j++) {
            centre = centrePoints[j];

            Arrays.fill(newPixels, 0);
            radius = 0;
            borderCount = 0;

            newPixels[0] = pixels[centre];

            radius++;

            radians = Math.PI / 2 * radius;

            cycleNum = (int) (2 * Math.PI / radians);

            stageInCycle = 0;

            for (int i = 1; i < 50000; i++) {

                pixRadii[i] = radius;

                x = (int) (radius * Math.cos((radians * stageInCycle)));
                y = (int) (radius * Math.sin((radians * stageInCycle)));
                stageInCycle++;

                doNotUse = false;

                if ((centre % imageWidth) + x < 0 || (centre % imageWidth) + x > imageWidth - 1)
                    doNotUse = true;
                else if ((centre / imageWidth) + y < 0 || (centre / imageWidth) + y > imageHeight - 1)
                    doNotUse = true;
                else if (pixels[centre + x + (y * imageWidth)] == Color.BLACK) {
                    borderCount++;
                }

                if (borderCount >= 500)
                    break;

                //            if (Math.abs(x) > imageWidth / 2 - 1
                //                    || Math.abs(y) > imageHeight / 2 - 1)
                //                doNotUse = true;

                if (!doNotUse) {
                    newPixels[i] = pixels[centre + x + (y * imageWidth)];
                }


                if (stageInCycle >= cycleNum) {
                    borderCount = 0;

                    radius++;

                    radians = Math.PI / (2 * radius);

                    cycleNum = (int) radius * 4;

                    stageInCycle = 0;
                }

            }

            pixels[centre] = newPixels[0];

            radius = 1;

            radians = Math.PI / 2 * radius;

            cycleNum = radius * 4;

            stageInCycle = 0;

            for (int i = 1; i < pixels.length; i++) {

                x = (int) (radius * Math.cos((radians * stageInCycle) - Math.PI / 38));
                y = (int) (radius * Math.sin((radians * stageInCycle) - Math.PI / 38));

                stageInCycle++;

                doNotUse = false;

                if ((centre % imageWidth) + x < 0 || (centre % imageWidth) + x > imageWidth - 1)
                    doNotUse = true;
                else if ((centre / imageWidth) + y < 0 || (centre / imageWidth) + y > imageHeight - 1)
                    doNotUse = true;

                //            if (Math.abs(x) > imageWidth / 2 - 1
                //                    || Math.abs(y) > imageHeight / 2 - 1)
                //                doNotUse = true;

                if (!doNotUse && newPixels[i] != 0)
                    pixStore[centre + x + (y * imageWidth)] = newPixels[i];

                if (stageInCycle > cycleNum) {
                    radius++;

                    radians = Math.PI / (2 * radius);

                    cycleNum = (int) (2 * Math.PI / radians);

                    stageInCycle = 0;
                }

            }

        }

        bit = Bitmap.createBitmap(pixStore, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;

    }

    public Bitmap multiCircle (Bitmap bit) {
        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        int[] pixels = new int[imageHeight * imageWidth];
        int[] pixStore = new int[pixels.length];
        int radius = 1;
        int x;
        int y;
        double tempX;
        double tempY;
        int centre = pixels.length / 2 + (imageWidth / 2);
        double radians;
        boolean doNotUse;
        double incRad = Math.PI / (2 * rayIntensity);
        int[] centrePoints = new int[numCentrePoints * numCentrePoints];
        int[] colours = new int[centrePoints.length];
        int centreCounter = 0;
        int borderCount = 0;
        int alreadySwitched;
        int[] blobPixels;

        bit.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        for (int j = 0; j < numCentrePoints; j++) {
            for (int i = 0; i < numCentrePoints; i++) {
                centrePoints[centreCounter] = pixels.length / 2 + ((i - numCentrePoints / 2) * imageWidth / numCentrePoints) + ((j - numCentrePoints / 2) * imageWidth * imageHeight / numCentrePoints);
                centreCounter++;
            }
        }

        Arrays.fill(pixStore, -1);

        for (int j = 0; j < centrePoints.length; j++) {
            centre = centrePoints[j];
            colours[j] = (j + 1) * -4500 - 1;
            blobPixels = new int[pixels.length];
            alreadySwitched = 0;

            if (pixStore[centre] != -1) {
                colours[j] = pixStore[j];
            }

            radians = incRad;

            tempX = Math.cos(radians);
            tempY = Math.sin(radians);

            for (int i = 1; i < pixels.length; i++) {

                x = (int) (tempX * radius);
                y = (int) (tempY * radius);

                radius++;

                doNotUse = false;
                if ((centre % imageWidth) + x < 0 || (centre % imageWidth) + x > imageWidth - 1)
                    doNotUse = true;
                else if ((centre / imageWidth) + y < 0 || (centre / imageWidth) + y > imageHeight - 1)
                    doNotUse = true;
                else if (pixels[centre + x + (y * imageWidth)] == Color.BLACK)
                    borderCount++;
                else if (pixStore[centre + x + (y * imageWidth)] != -1
                        && pixStore[centre + x + (y * imageWidth)] != colours[j]) {
                    doNotUse = true;
                    if (alreadySwitched < switchLimit) {
                        colours[j] = pixStore[centre + x + (y * imageWidth)];
                    }

                    alreadySwitched++;
                }

                if (!doNotUse && borderCount < borderLimit) {
                    blobPixels[i] = centre + x + (y * imageWidth);
                } else {
                    borderCount = 0;
                    radians += incRad;

                    radius = 1;

                    tempX = Math.cos(radians);
                    tempY = Math.sin(radians);
                }

                if (radians > 2 * Math.PI) {
                    blobPixels[i + 1] = -2;
                    break;
                }

            }

            pixStore[centre] = colours[j];

            for (int i = 0; i < blobPixels.length; i++) {

                if (blobPixels[i] == -2)
                    break;

                pixStore[blobPixels[i]] = colours[j];

            }

        }

        bit = Bitmap.createBitmap(pixStore, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;

    }

}