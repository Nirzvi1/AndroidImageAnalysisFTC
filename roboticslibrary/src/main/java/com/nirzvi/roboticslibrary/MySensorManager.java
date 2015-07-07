package com.nirzvi.roboticslibrary;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Created by Nirzvi on 2015-05-22.
 */
public class MySensorManager {

    public final static int ACCEL = 151432;
    public final static int MAG = 38294;
    public final static int ORIENT = 23484;
    public final static int GYRO = 82354;
    public final static int GRAV = 21305;
    public final static int LIGHT = 43905;
    public final static int TEMP = 4237413;
    public final static int PROX = 4321234;
    public final static int LINEAR_ACC = 302492;

    public final static int AZIMUTH = -123144;
    public final static int PITCH = 432245;
    public final static int ROLL = 321445;


    int count = 0;
    SensorManager sm;
    MySensor[] sense;
    String[] names;
    boolean nameSame = false;

    public MySensorManager (Context c) {
        sm = (SensorManager) c.getSystemService(c.SENSOR_SERVICE);
        sense = new MySensor[20];
        names = new String [20];
    }

    public void getRotation (float[] firstFloat, float[] secondFloat, float[] gravFloat, float[] magFloat) {

        sm.getInclination(secondFloat);

        sm.getRotationMatrix(firstFloat, secondFloat, gravFloat, magFloat);
    }

    public void getOrientation (float[] firstFloat, float[] secondFloat) {
        sm.getOrientation(firstFloat, secondFloat);
    }

    public float getAngles (String accelName, String magName, int axis) {
        float[] rotMatrix = new float[9];
        float[] incline = new float[9];
        float[] orientMatrix = new float[3];

        getRotation(rotMatrix, incline, getValues(accelName), getValues(magName));
        getOrientation(rotMatrix, orientMatrix);

        if (axis == AZIMUTH)
            axis = 0;
        else if (axis == PITCH)
            axis = 1;
        else if (axis == ROLL)
            axis = 2;

        if (orientMatrix[axis] < 0) {
            orientMatrix[axis] = (float) (Math.PI - Math.abs(orientMatrix[axis]));
            orientMatrix[axis] += Math.PI;
        }

        orientMatrix[axis] = (float) Math.toDegrees(orientMatrix[axis]);

        return orientMatrix[axis];
    }

    public void addSensor (int sensor, String name) {

        for (int i = 0; i < 20; i++) {
            if (names[i] == name) {
                nameSame = true;
                break;
            }
        }

        if (++count < 20 && !nameSame) {
            sense[count] = new MySensor(sm, sensor);
            names[count] = name;
        }
        else if (count >= 20 && !nameSame)
           try {
               throw new Exception("Too many sensors!");
           } catch (Exception e) {

           }
    }

    public void removeSensor (String name) throws Exception {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                sense[i] = null;
                return;
            }
        }

        throw new Exception ("No sensor by that name");
    }

    public float[] getValues (String name) {
        int index = 0;

        for (int i = 0; i < 20; i++) {
            if (names[i] == name) {
                index = i;
                break;
            }
        }

        return sense[index].getSensorValues();
    }

    public String getSensorType(String name) {
        int index = 0;

        for (int i = 0; i < count; i++) {
            if (names[i] == name) {
                index = i;
                break;
            }
        }

        switch (sense[index].getTypeOfSensor()) {
            case MAG: return "magnetic field";
            case ACCEL: return "accelerometer";
            case ORIENT: return "orientation";
            case GYRO: return "gyroscope";
            case GRAV: return "gravity";
            case LIGHT: return "light";
            case TEMP: return "temperature";
            case PROX: return "proximity";
        }

        return null;
    }
}

class MySensor {


    final int ACCEL = 151432;
    final int MAG = 38294;
    final int ORIENT = 23484;
    final int GYRO = 82354;
    final int GRAV = 21305;
    final int LIGHT = 43905;
    final int TEMP = 4237413;
    final int PROX = 4321234;
    final int LINEAR_ACC = 302492;

    int sensor = 0;
    int numValues = 0;
    float[] sensorValues;
    Sensor sense;

    SensorEventListener senseListen = new SensorEventListener() {

        @Override
        public void onAccuracyChanged (Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged (SensorEvent event) {

            for (int i = 0; i < numValues; i++) {
                sensorValues[i] = event.values[i];
            }

        }
    };

    public MySensor (SensorManager sm, int sensor) {

        switch (sensor) {
            case MAG: sense = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                numValues = 3;
                break;
            case ACCEL: sense = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                numValues = 3;
                break;
            case ORIENT: sense = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
                numValues = 3;
                break;
            case GYRO: sense = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                numValues = 3;
                break;
            case GRAV: sense = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
                numValues = 3;
                break;
            case LIGHT: sense = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
                numValues = 1;
                break;
            case TEMP: sense = sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
                numValues = 1;
                break;
            case PROX: sense = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                numValues = 1;
                break;
            case LINEAR_ACC: sense = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                numValues = 3;
                break;
        }

        sm.registerListener(senseListen, sense, SensorManager.SENSOR_DELAY_FASTEST);

        sensorValues = new float[numValues];
    }

    public int getTypeOfSensor() {

       return sensor;

    }

    public float[] getSensorValues() {

        return sensorValues;

    }



}