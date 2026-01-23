package org.firstinspires.ftc.teamcode.decode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Transfer {
    public Servo stage2;
    public double stage2Start = 0.6;
    public double stage2Push = 0.2;
    public double pushTime = 0.5;

    public Transfer(HardwareMap hardwareMap) {
        stage2 = hardwareMap.get(Servo.class, "stage2");
    }
}
