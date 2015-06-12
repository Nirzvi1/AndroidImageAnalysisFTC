package com.nirzvi.roboticsproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Alec on 6/11/2015.
 */
public class AlecImage {

    public AlecImage(){

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
        int a = 105;
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
        for(int y = 0; y < height; y++){
            colorold = standardColor(original.getPixel(0, y));
            for(int x = 0; x < width; x++){
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

    public Bitmap findBlobCircle(Bitmap img, int xI, int yI){
        int width = img.getWidth();
        int height = img.getHeight();
        int check = 0;
        int x = 0;
        int y = 0;
        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        color.setColor(Color.WHITE);
        pic.drawRect(0, 0, width, height, color);
        color.setColor(Color.BLACK);

        for(int i = 0; i < check; i++){

        }

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
                pic.drawRect(x, y, x + 1, y + 1, color);
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
        int colorold = standardColor(original.getPixel(0, 0));
        Bitmap bitsy =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint color = new Paint();
        Canvas pic = new Canvas(bitsy);
        for(int x = 0; x < width; x++){
            colorold = standardColor(original.getPixel(x, 0));
            for(int y = 0; y < height; y++){
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
