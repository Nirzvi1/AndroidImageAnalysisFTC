package com.nirzvi.roboticslibrary;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

/**
 * Created by Nirzvi on 2015-05-23.
 */
public class MyDataWriter {

    File file;
    FileOutputStream stream;
    String toWrite = "";
    MyDataReader reader;

    public MyDataWriter (String fileName, boolean overWrite, Context c) {
        reader = new MyDataReader(fileName, c);

        file = new File(c.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName + ".txt");

        try {
            stream = new FileOutputStream(file);
        } catch (Exception e) {
        }

        toWrite = "";

        if (!overWrite) {
            toWrite = reader.readFile();
        }
        System.out.println(toWrite + "--------------------");

    }

    public MyDataWriter (String filePath, String fileName, boolean overWrite, Context c) {
        if (!filePath.contains("txt"))
            file = new File(c.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName + ".txt");
        else
            file = new File(filePath);

        try {
            stream = new FileOutputStream(file);
        } catch (Exception e) {

        }

        if (!overWrite) {
            MyDataReader in = new MyDataReader(fileName, c);
            if (in.readFile() != null)
                toWrite += in.readFile();
        }
        System.out.println(toWrite + "--------------------");

    }

    public void write (String data) {

        toWrite += data;

        try {
            stream.write(toWrite.getBytes());
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void write (int data) {

        toWrite = "" + data;

        try {
            stream.write(toWrite.getBytes());
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void write (double data) {

        toWrite = "" + data;

        try {
            stream.write(toWrite.getBytes());
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void write (float data) {

        toWrite = "" + data;

        try {
            stream.write(toWrite.getBytes());
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void write (long data) {

        toWrite = "" + data;

        try {
            stream.write(toWrite.getBytes());
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void write (short data) {

        toWrite = "" + data;

        try {
            stream.write(toWrite.getBytes());
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void write (byte data) {

        toWrite = "" + data;

        try {
            stream.write(data);
        } catch (Exception e) {

        }

        toWrite = "";
    }

    public void close () {
        try {
            stream.close();
        } catch (Exception e) {

        }
    }

}
