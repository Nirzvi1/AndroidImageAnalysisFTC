package com.nirzvi.roboticslibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nirzvi on 2015-06-23.
 */
public class OldCode {


    public final int OUTLINES = 1500000;
    public final int DETAILS = 300000;
    public int accuracy = DETAILS;
    public int blobAccuracy = 2;
    public int threshold = 0;

    public int nextColour (int colourCount) {

        int numColour = 1000;

        if (colourCount % 7 == 0)
            return Color.argb(255, colourCount * 255 / numColour, 0, 0);
        else if (colourCount % 7 == 1)
            return Color.argb(255, 0, colourCount * 255 / numColour, 0);
        else if (colourCount % 7 == 2)
            return Color.argb(255, 0, 0, colourCount * 255 / numColour);
        else if (colourCount % 7 == 3)
            return Color.argb(255, 0, colourCount * 255 / numColour, colourCount * 255 / numColour);
        else if (colourCount % 7 == 4)
            return Color.argb(255, colourCount * 255 / numColour, 0, colourCount * 255 / numColour);
        else if (colourCount % 7 == 5)
            return Color.argb(255, colourCount * 255 / numColour, colourCount * 255 / numColour, 0);
        else
            return Color.argb(255, colourCount * 255 / numColour, colourCount * 255 / numColour, colourCount * 255 / numColour);

    }


    public static int checkIfNear(int[] pixels, int pixelCoord, int imageWidth) {

        for (int i = 0; i < pixels.length; i++) {

            if (pixelCoord == pixels[i] + imageWidth) {
                return 0;
            } else if (pixelCoord == pixels[i] + 1) {
                return 1;
            } else if (pixelCoord == pixels[i] - imageWidth) {
                return 2;
            } else if (pixelCoord == pixels[i] - 1) {
                return 3;
            }
        }

        return -1;
    }

    public int checkAround (int[] pixColours, int pixelCoord, int imageWidth) {


        return -1;
    }


    public Bitmap blobsCircle (Bitmap bit) {
        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        int[] pixels = new int[imageHeight * imageWidth];
        int[] pixStore = new int[pixels.length];
        int[] newPixels = new int[pixels.length];
        int radius = 0;
        int x;
        int y;
        int center = pixels.length / 2 + (imageWidth / 2);
        double radians = 0;
        int stageInCycle = 0;
        int cycleNum = 1;
        boolean doNotUse = false;
        int[] pixRadii = new int[pixels.length];

        bit.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        for (int i = 0; i < newPixels.length; i++) {
            pixStore[i] = -1;
        }

        newPixels[0] = pixels[center];

        radius++;

        radians = Math.PI / 2 * radius;

        cycleNum = (int) (2 * Math.PI / radians);

        stageInCycle = 0;

        for (int i = 1; i < pixels.length; i++) {

//            if (radius > 190)
//                break;

            stageInCycle++;

            pixRadii[i] = radius;

            x = (int) (radius * Math.cos(radians * stageInCycle));
            y = (int) (radius * Math.sin(radians * stageInCycle));

            doNotUse = false;

            if (Math.abs(x) > 298)
                doNotUse = true;
            else if (Math.abs(y) > 181)
                doNotUse = true;

            //Log.i("Radius", "" + radius);

            if (!doNotUse) {
                newPixels[i] = pixels[center + x + (y * imageWidth)];
            }

            if (stageInCycle > cycleNum) {
                radius++;

                radians = Math.PI / (2 * radius);

                cycleNum = (int) radius * 4;

                stageInCycle = 0;
            }

        }


        for (int i = 1; i < newPixels.length; i++) {

            int downOne = i - (i % (pixRadii[i] * 4)) - ((i / (pixRadii[i] * 4)) * (pixRadii[i] - 1) * 4);

            if (Math.abs(newPixels[i] - newPixels[i - 1]) < accuracy)
                newPixels[i] = newPixels[i - 1];
            else if (Math.abs(newPixels[i] - newPixels[downOne]) < accuracy)
                newPixels[i] = newPixels[downOne];
        }

        pixels[center] = newPixels[0];

        radius = 1;

        radians = Math.PI / 2 * radius;

        cycleNum = (int) (2 * Math.PI / radians);

        stageInCycle = 0;

        for (int i = 1; i < pixels.length; i++) {

//            if (radius > 190)
//                break;

            stageInCycle++;

            pixRadii[i] = radius;

            x = (int) (radius * Math.cos(radians * stageInCycle));
            y = (int) (radius * Math.sin(radians * stageInCycle));

            doNotUse = false;

            if (Math.abs(x) > 298)
                doNotUse = true;
            else if (Math.abs(y) > 181)
                doNotUse = true;

            //Log.i("Info", "Radius: " + radius + " CycleNum: " + cycleNum + "Stage: " + stageInCycle + " Radians: " + radians);

            if (!doNotUse)
                pixStore[center + x + (y * imageWidth)] = newPixels[i];

            if (stageInCycle > cycleNum) {
                radius++;

                radians = Math.PI / (2 * radius);

                cycleNum = (int) (2 * Math.PI / radians);

                stageInCycle = 0;
            }

        }

        bit = Bitmap.createBitmap(pixStore, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;

    }
    public Bitmap getBlobs (Bitmap bit) {

        int imageWidth = bit.getWidth();
        int imageHeight = bit.getHeight();
        int[] pixels = new int[imageHeight * imageWidth];
        int[] pixBlob = new int[pixels.length];
        int[] imgPixels = new int[pixels.length];
        int[] pixColours = new int[pixels.length];
        List<List<Integer>> blobs = new ArrayList<>();
        int blobCount = 0;
        boolean[] badBlobs = new boolean[pixels.length];

        pixels = getEdgeInts(bit);
        bit.getPixels(imgPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        blobs.add(new ArrayList<Integer>());
        for (int i = 0; i < pixels.length; i++) {

            if (i % imageWidth > 0 && pixels[i] == Color.BLACK && pixels[i - 1] == Color.BLACK) {
                blobCount++;
                blobs.add(blobCount, new ArrayList<Integer>());
            }

            blobs.get(blobCount).add(i);

            pixBlob[i] = blobCount;

            imgPixels[i] = closeToColour(imgPixels[i]);

        }


        for (int j = 0; j < pixels.length; j++) {

            if (j >= imageWidth && imgPixels[j] == imgPixels[j - imageWidth]) {

                try {
                    for (int i = 0; i < blobs.get(pixBlob[j - imageWidth]).size(); i++) {

                        blobs.get(pixBlob[j]).add(blobs.get(pixBlob[j]).get(i));

                    }
                    badBlobs[j - imageWidth] = true;
                } catch (Error e) {

                }

            }

        }

        for (int k = 0; k < blobs.size(); k++) {

            if (!badBlobs[k]) {

                for (int l = 0; l < blobs.get(k).size(); l++) {

                    pixColours[blobs.get(k).get(l)] = nextColour(k);

                }

            }

        }

        bit = Bitmap.createBitmap(pixColours, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;

       /* pixels = getEdgeInts(bit);
        bit.getPixels(imgPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        for (int i = 0; i < pixels.length; i++){

            doNotPaint = false;
            if (i % imageWidth > 0 && pixels[i] == Color.BLACK //&& pixels[i - 1] == Color.BLACK
//                    && pixels[i - 2] == Color.BLACK && pixels[i - 3] == Color.BLACK
//                    && pixels[i - 4] == Color.BLACK && pixels[i - 5] == Color.BLACK
//                    && pixels[i - 6] == Color.BLACK && pixels[i - 7] == Color.BLACK
//                    && pixels[i - 8] == Color.BLACK && pixels[i - 9] == Color.BLACK
                ) {
                currentColour = nextColour(colourCount);
                colourCount++;
                storePixels[i] = Color.TRANSPARENT;
            } else
                pixColours[i] = currentColour;

        }

//        for (int i = 0; i < pixels.length; i += imageWidth) {
//
//            if (i >= imageWidth && pixels[i - imageWidth] != Color.BLACK)
//                pixColours[i] = pixColours[i - imageWidth];
//
//            if (i == pixels.length - 1)
//                break;
//
//            if (i / imageWidth == imageHeight - 1) {
//                //Log.i("I", "" + i);
//                i = i % imageWidth + 1;
//            }
//
//        }

        bit = Bitmap.createBitmap(pixColours, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;*/
    }


    public int closeToColour (int colour) {

        int closestColour;

        //Log.i("First Colour", "" + colour);

        closestColour = Color.argb(255, Color.red(colour) - (Color.red(colour) % (255 / blobAccuracy)),
                Color.green(colour) - (Color.green(colour) % (255 / blobAccuracy)),
                Color.blue(colour) - (Color.blue(colour) % (255 / blobAccuracy)));

        return closestColour;
    }

    public int[] getEdgeInts (Bitmap bit) {

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

        newBit.getPixels(pixels, 0, newBit.getWidth(), 0, 0, newBit.getWidth(), newBit.getHeight());

        return pixels;
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

    public int averageColour(int[] colours) {
        long greenF = 0;
        long redF = 0;
        long blueF = 0;

        int numPixels = 0;
        for (int i = 0; i < colours.length; i++) {
            redF += Color.red(colours[i]);
            blueF += Color.blue(colours[i]);
            greenF += Color.green(colours[i]);
        }

        redF /= colours.length;
        greenF /= colours.length;
        blueF /= colours.length;

        return Color.argb(255, (int) redF, (int) greenF, (int) blueF);
    }

    public int checkDirections(int i, int width, int height, int[] pixels) {

        if (i == 0)
            return -1;

        if (i % width < width - 1)
            for (int j = 0; j < width - (i % width); j++) {
                if (pixels[i + j] == Color.GREEN) {
                    break;
                }
                if (j == width - (i % width) - 1)
                    return 1;
            }

        if (i % width > 1)
            for (int j = 0; j < i % width; j++) {
                if (pixels[i - j] == Color.GREEN) {
                    break;
                }
                if (j == width - (i % width) - 1)
                    return 3;
            }

        if (i / width > 1)
            for (int j = (i % width); j < (i / width); j++) {
                if (pixels[i - (j * width)] == Color.GREEN) {
                    break;
                }
                if (j == i / width - 1)
                    return 0;
            }

        if (i / width < height - 1)
            for (int j = (i % width); j < height - (i / width); j++) {
                if (pixels[i + (j * width)] == Color.GREEN) {
                    break;
                }
                if (j == height - (i / width) - 1)
                    return 2;
            }

        return -1;
    }


    public Bitmap closestColour (Bitmap bit) {

        int imageWidth = 600;
        int imageHeight = 350;
        int[] pixels = new int[imageWidth * imageHeight];
        int[] pixColour = new int[pixels.length];
        int colour = 0;
        int counter = 0;
        int numBlur = 50;
        int[] colours = new int[(int ) Math.pow(numBlur, 2)];
        int[] coords = new int[colours.length];

        //pixels = getEdgeInts(bit);
        bit.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        Log.i("WIDTH", "" + imageHeight);

        for (int i = 0; i < pixels.length; i += numBlur) {

            for (int j = 0; j < numBlur; j++) {

                for (int k = 0; k < (1 + (j * 2)); k++) {

                    if (k < (2 + (j * 2)) / 2) {
                        colours[counter] = pixels[i + j + imageWidth * k];
                        coords[counter] = i + j + imageWidth * k;
                    } else {
                        colours[counter] = pixels[i + (2 * j - k)];
                        coords[counter] = i + (2 * j - k);
                    }
                    counter++;

                }

            }

            colour = averageColour(colours);

            for (int j = 0; j < colours.length; j++) {
                pixColour[coords[j]] = colour;
            }

            if (i % imageWidth == imageWidth - numBlur)
                i += (imageWidth * (numBlur - 1));


            counter = 0;
        }

        for (int i = 0; i < pixels.length; i++)
            pixColour[i] = closeToColour(pixels[i]);

        bit = bit.createBitmap(pixColour, 0, imageWidth, imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        return bit;
    }

    public int standgrdColor(int color){
        color /= 10000;
        return color;
    }
    public boolean imageContains(Bitmap full, Bitmap inner){
        int width = inner.getWidth();
        int height = inner.getHeight();
        for(int y = 0; y < full.getHeight() - height; y++){
            for(int x = 0; x < full.getWidth() - width; x++){
                Bitmap cropedBitmap = Bitmap.createBitmap(full, x, y, width, height);
                if(imageEquals(cropedBitmap, inner)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean imageEquals(Bitmap img1, Bitmap img2){
        if(img1.getHeight() != img2.getHeight() || img1.getWidth() != img2.getWidth()) return false;

        for(int y = 0; y < img1.getHeight(); y++){
            for(int x = 0; x < img1.getWidth(); x++){
                if(img1.getPixel(x, y) != img2.getPixel(x, y)){
                    return false;
                }
            }
        }
        return true;
    }

    public int standardColor(int color){
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);
        int a = threshold;
        if(red >= a && blue < a && green < a) return Color.RED;
        else if(red >= a && blue >= a && green < a) return Color.MAGENTA;
        else if(red >= a && blue >= a && green >= a) return Color.WHITE;
        else if(red >= a && blue < a && green >= a) return Color.YELLOW;
        else if(red < a && blue >= a && green >= a) return Color.CYAN;
        else if(red < a && blue < a && green >= a) return Color.GREEN;
        else if(red < a && blue >= a && green < a) return Color.BLUE;
        else if(red < a && blue < a && green < a) return Color.BLACK;
        return Color.BLACK;
    }

    public int [] convert(Bitmap img, int threshold){
        int width = img.getWidth();
        int height = img.getHeight();
        int [] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        for(int i = 0; i < pixels.length; i++){
            int r = (Color.red(pixels[i]) - 10 / 4) * 4;
            int g = ((Color.green(pixels[i]) + 10) / 4) * 4;
            int b = (Color.blue(pixels[i]) / 4) * 4;
            pixels[i] = Color.rgb(r, g, b);
        }
        return pixels;
    }

    public String colorName(int color){
        switch(color){
            case Color.RED: return "red";
            case Color.GREEN: return "green";
            case Color.MAGENTA: return "magenta";
            case Color.CYAN: return "cyan";
            case Color.WHITE: return "white";
            case Color.YELLOW: return "yellow";
            case Color.BLACK: return "black";
            case Color.BLUE: return "blue";
        }
        return "not standard";
    }

    public Bitmap robotSees(Bitmap original){
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                color.setColor(standardColor(original.getPixel(x, y)));
                pic.drawRect(x, y, x + 1, y + 1, color);
            }
        }
        return bitsy;
    }

    public Bitmap edgesV(Bitmap original){
        int width = original.getWidth();
        int height = original.getHeight();
        int colorold = standardColor(original.getPixel(0, 0));
        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        for(int x = 0; x < height; x++){
            colorold = standardColor(original.getPixel(x, 0));
            for(int y = 0; y < width; y++){
                int tempColor = standardColor(original.getPixel(x, y));
                if(tempColor == colorold) {
                    color.setColor(Color.WHITE);
                } else {
                    color.setColor(Color.BLACK);
                }
                pic.drawRect(x, y, x + 1, y + 1, color);
                colorold = tempColor;
            }
        }
        return bitsy;
    }

    public void findBlobs(List<Blob> blobs, Bitmap img ){
        int blobdex = 0;
        int width = img.getWidth();
        int height = img.getHeight();
        int [] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);
//        blobs.clear();
//        blobs.add(new Blob(pixels[0]));
//       for(int i = 0; i < pixels.length; i++){
//           Blob blob = blobs.get(blobdex);
//           if(blob.color == pixels[i]){
//               blob.addCoord(new Coord(i % width, i / width, i));
//           } else{
//               blobs.add(new Blob(new Coord(i % width, i / width), pixels[i]));
//               blobdex++;
//           }
//       }
        long timer = new Date().getTime();
        Log.d("Size", "" + blobs.size());
        Log.d("Size", "" + width * height);
        Log.d("Time", "" + timer);
        for(int j = 0; j < blobs.size(); j++) {
            Blob blob = blobs.get(j);
            long time = new Date().getTime();
            for(int h = 0; h < blob.points.size(); h++) {
                Coord c = blob.points.get(h);
                if(c.index >= pixels.length - width - 1){
                    h++;
                    continue;
                }
                if(pixels[c.index + width] == blob.color){
                    int index = blobWith(blobs, new Coord(c.x, c.y + 1), new Coord(0, 0))[0];
                    if(index != -1) {
                        if (blobs.get(index).color == blob.color && index != j) {
                            blob.merge(blobs.get(index));
                            blobs.remove(index);
                            Log.d("merged", "" + j + ", " + index);
                        }
                    }
                }

            }
            Log.d("length", "Blob " + j + " " + blob.size);

            Log.d("color" , colorName(blob.color));
            Log.d("Time", "Blob " + j + " " + ((new Date().getTime() - time) / 1000) + "s");
        }
//        for(int x = 0; x < width - 1; x++){
//
//            Log.d("Progress", "" + x / width * 100.0  + "%");
//            for(int y = 0; y < height - 1; y++){
//                int [] temp = blobWith(blobs, new Coord(x, y), new Coord(x, y + 1));
//                int in1 = temp[0];
//                int in2 = temp[1];
//                if(in1 != in2 && in1 != -1 && in2 != -1) {
//                    Blob one = blobs.get(in1);
//                    Blob two = blobs.get(in2);
//                    if(one.color == two.color){
//                        one.merge(two);
//                        blobs.remove(two);
//                        Log.d("merged", "" + in1 + ", " + in2);
//                    }
//                }
//            }
//        }
        Log.d("Size", "" + blobs.size());
        Log.d("Time", "" + (new Date().getTime() - timer));

        Bitmap bitsy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitsy);
        for(int j = 0; j < blobs.size(); j++) {
            Paint color = new Paint();
            color.setColor(j * -4500 - 10);
            for (int i = 0; i < blobs.get(j).size; i++) {
                Coord c = blobs.get(j).points.get(i);
                can.drawRect(c.x, c.y, c.x + 1, c.y + 1, color);
            }
        }

    }

    public int[] blobWith(List<Blob> blobs, Coord coord1, Coord coord2){
        int [] indeces = {-1, -1};

        for(int i = 0; i < blobs.size(); i++){
            if(blobs.get(i).hasCoord(coord1)) indeces[0] = i;
            if(blobs.get(i).hasCoord(coord2)) indeces[1] = i;
            if(indeces[0] != -1 && indeces[1] != -1){
                return indeces;
            }
        }
        return indeces;
    }

    public Bitmap blobTest(){
        Bitmap bitsy =  Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        color.setColor(Color.WHITE);
        pic.drawRect(0, 0, 50, 50, color);
        color.setStyle(Paint.Style.STROKE);
        color.setColor(Color.BLACK);
        pic.drawCircle(20, 20, 10, color);
        pic.drawCircle(20, 20, 5, color);
        pic.drawRect(5, 5, 20, 20, color);
        return bitsy;
    }

    public Bitmap findBlob(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        color.setColor(Color.WHITE);
        pic.drawRect(0, 0, width, height, color);

        color.setColor(Color.BLACK);
        for(int x = 0; x < width; x++){
            if(img.getPixel(x, 0) == Color.BLACK){
                break;
            }//if
            for(int y = 0; y < height; y++){
                pic.drawRect(x, y, x + 1, y + 1, color);
                if(img.getPixel(x, y) == Color.BLACK){
                    break;
                }//if
            }//for
        }//for
        for(int y = 0; y < height; y++){
            if(img.getPixel(y, 0) == Color.BLACK){
                break;
            }//if
            for(int x = 0; x < width; x++){
                pic.drawRect(x, y, x + 2, y + 2, color);
                if(img.getPixel(x, y) == Color.BLACK){
                    break;
                }//if
            }//for
        }//for
        return bitsy;
    }

    public Bitmap edgesH(Bitmap original){
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        int [] pixels = new int[width * height];
        pixels = convert(original, threshold);
        original.getPixels(pixels, 0, width, 0, 0, width, height);
        for(int i = 1; i < pixels.length; i++){
            if(i % width == width - 1){
                i++;
                continue;
            }
            pixels[i] = standardColor(pixels[i]);
            if(pixels[i] == pixels[i - 1]) {
                color.setColor(Color.WHITE);
            } else {
                color.setColor(Color.BLACK);
            }
            pic.drawRect(i % width, i / width, i % width + 10, i / width + 10, color);
        }
        return bitsy;
    }

    public Bitmap superImpose(Bitmap img1, Bitmap img2) throws Exception {
        int width = img1.getWidth();
        int height = img1.getHeight();
        if(width != img2.getWidth() || height != img2.getHeight()){
            throw new Exception("Images not the same size");
        }//if

        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int tempColor1 = img1.getPixel(x, y);
                int tempColor2 = img2.getPixel(x, y);
                if(tempColor1 == tempColor2) {
                    color.setColor(tempColor1);
                } else {
                    color.setColor(Color.BLACK);
                }//else
                pic.drawRect(x, y, x + 1, y + 1, color);
            }//for
        }//for
        return bitsy;
    }

}

class Blob {

    List<Coord> points = new ArrayList<>();
    int color;
    int size;

    public void merge(Blob blob) {

    }

    public boolean hasCoord (Coord c) {

        return false;
    }

}

class Coord {

    int index = 0;
    int x = 0;
    int y = 0;

    public Coord (int x, int y) {


    }

}
