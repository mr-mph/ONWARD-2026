package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "!! 3 Specimen Close Auto ", group = "! Auto")
public class CloseAuto2 extends LinearOpMode

{

    @Override
    public void runOpMode() throws InterruptedException
    {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(0, 0), 0));

        DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0,0,0))
                        .lineToX(50)
                        .lineToX(0)
                        .build());
    }
}