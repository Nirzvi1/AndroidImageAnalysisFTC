package com.nirzvi.roboticslibrary;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Nirzvi on 2015-05-23.
 */
public class MyDataReader {

    BufferedReader in;
    int numLines;
    String data = "";
    StringBuilder sb = new StringBuilder();

    public MyDataReader (String fileName, Context c) {
        try {
            in = new BufferedReader(new FileReader(new File(c.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName + ".txt")));
            in.mark(Short.MAX_VALUE);

            while (in.readLine() != null) {
                numLines++;
            }
            in.reset();
        } catch (Exception e) {

        }

    }

    public String readFile () {
        try {
            for (int i = 0; i < numLines; i++) {
                data += in.readLine() + "\n";
            }
        } catch (Exception e) {
            return "";
        }

        return data;
    }

}
