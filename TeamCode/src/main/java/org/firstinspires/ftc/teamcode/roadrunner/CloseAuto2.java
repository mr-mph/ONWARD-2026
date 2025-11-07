package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
        DcMotorEx launcherLeft = hardwareMap.get(DcMotorEx.class, "launcher left");
        DcMotorEx launcherRight = hardwareMap.get(DcMotorEx.class, "launcher right");
        launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose2d startPos = new Pose2d(new Vector2d(0, 0), 0);

        waitForStart();

        TrajectoryActionBuilder line1 = drive.actionBuilder(startPos)
                .strafeTo(new Vector2d(50, 0))
                .endTrajectory();
        TrajectoryActionBuilder line2 = line1.fresh()
                .strafeTo(new Vector2d(50, 0))
                .endTrajectory();



        Actions.runBlocking(new SequentialAction(
                line1.build(),
                new InstantAction(() -> {
                    intake.setPower(1);
                }),
                line2.build(),
                new InstantAction(() -> {
                    intake.setPower(0);
                }),
                new InstantAction(() -> {
                    launcherLeft.setPower(1);
                    launcherRight.setPower(-1);
                })
        ));
    }
}