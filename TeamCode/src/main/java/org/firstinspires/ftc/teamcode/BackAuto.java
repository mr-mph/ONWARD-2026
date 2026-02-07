package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(name = "secondary auto", group = "! Auto")
public class BackAuto extends LinearOpMode {

	DcMotorEx intake, launcherLeft, launcherRight;
	Servo launcher, stage2;
	MecanumDrive drive;

	@Override
	public void runOpMode() {

		telemetry = new MultipleTelemetry(
				telemetry,
				FtcDashboard.getInstance().getTelemetry()
		);

		intake = hardwareMap.get(DcMotorEx.class,"intake");
		launcher = hardwareMap.get(Servo.class,"launcher");
		stage2 = hardwareMap.get(Servo.class,"stage2");
		launcherLeft = hardwareMap.get(DcMotorEx.class,"launchLeft");
		launcherRight = hardwareMap.get(DcMotorEx.class,"launchRight");

		launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		Pose2d startPos = new Pose2d(0, 0, Math.toRadians(90));
		drive = new MecanumDrive(hardwareMap, startPos);

		launcher.setPosition(LaunchConstants.unlaunchedPos);
		stage2.setPosition(LaunchConstants.stage2Start);

		waitForStart();

		TrajectoryActionBuilder line1 = drive.actionBuilder(startPos) // backup to shoot
				.strafeTo(new Vector2d(0,5))
				.endTrajectory();


		// ---------- Auto ----------
		Actions.runBlocking(new SequentialAction(
				line1.build()
		));
	}
}
