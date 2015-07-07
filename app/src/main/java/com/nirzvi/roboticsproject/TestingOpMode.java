package com.nirzvi.roboticsproject;

import com.nirzvi.roboticslibrary.MySensorManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;

/**
 * Created by Nirzvi on 2015-06-27.
 */
public class TestingOpMode extends OpMode {

    MySensorManager sm;

    DcMotor left;
    DcMotor right;
    DcMotor.MotorCallback leftEnc;
    int stage = 0;

    @Override
    public void start() {

        sm = new MySensorManager(hardwareMap.appContext);

        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

    }

    @Override
    public void loop() {

        if (stage == 0) {
            left.setPower(0.5);
            right.setPower(-0.5);
            stage++;
        } else if (stage == 1 && sm.getAngles("accel", "mag", sm.AZIMUTH) < 180) {
            left.setPower(0);
            right.setPower(0);
            stage++;
        }

    }

    @Override
    public void stop() {

    }
}
