package org.firstinspires.ftc.teamcode.decode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Launcher {
    public boolean pushing = false;
    public boolean launching = false;
    public boolean isMultiplying = false;

    public double unlaunchedPos = 0.35;
    public double settledPos = 0.35;
    public double launchedPos = 1;
    public double launchPower = 1430;
    public double multiplicity = 1.5;
    public double powerMultiplier = 1450;
    public double warmupTime = 0.3;
    public double settlingTime = 0.5;

    public Servo launcher;
    public DcMotorEx launcherLeft;
    public DcMotorEx launcherRight;

    public Launcher(HardwareMap hardwareMap) {
        launcher = hardwareMap.get(Servo.class, "launcher");
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launchLeft");
        launcherRight = hardwareMap.get(DcMotorEx.class, "launchRight");

        launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        launcherLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
