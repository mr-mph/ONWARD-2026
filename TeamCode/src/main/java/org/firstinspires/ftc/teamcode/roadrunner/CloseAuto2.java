package org.firstinspires.ftc.teamcode.roadrunner;

import com.acmerobotics.dashboard.config.Config;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.PinpointLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;


@Config
@Autonomous(name = "!! 3 Specimen Close Auto ", group = "! Auto")
public class CloseAuto2 extends LinearOpMode

{

    @Override
    public void runOpMode()
    {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(55.7010989010989, 6.804395604395612), 90));

        DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");

        Pose2d startPose = new Pose2d(55.7010989010989,6.804395604395612, Math.toRadians(90));


        waitForStart();

        TrajectoryActionBuilder line1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(56, 36))
                .endTrajectory();

        TrajectoryActionBuilder line2 = line1.fresh()
                .strafeTo(new Vector2d(14.716483516483516, 35.28791208791209))
                .endTrajectory();


        Actions.runBlocking(new SequentialAction(
                line1.build(),
                new InstantAction(() -> {intake.setPower(1);} ),
                line2.build(),
                new InstantAction(() -> {intake.setPower(0);} )
        ));
    }


}