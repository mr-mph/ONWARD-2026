package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.*;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(name = "! quin auto", group = "! Auto")
public class QuinAuto extends LinearOpMode {

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

		Pose2d startPos = new Pose2d(-51, -48, Math.toRadians(54));
		drive = new MecanumDrive(hardwareMap, startPos);

		launcher.setPosition(LaunchConstants.unlaunchedPos);
		stage2.setPosition(LaunchConstants.stage2Start);

		waitForStart();

		Action line1 = drive.actionBuilder(startPos)
				.strafeTo(new Vector2d(-34.5, -33.1))
				.build();

		Action line2 = drive.actionBuilder(new Pose2d(-34.5, -33.1, Math.toRadians(54)))
				.strafeTo(new Vector2d(-17, -17.8))
				.turnTo(Math.toRadians(-90))
				.build();

		Action line3 = drive.actionBuilder(new Pose2d(-17, -17.8, Math.toRadians(-90)))
				.strafeTo(new Vector2d(-17, -47.3))
				.build();

		Action line4 = drive.actionBuilder(new Pose2d(-17, -47.3, Math.toRadians(-90)))
				.strafeTo(new Vector2d(-34.5, -33.1))
				.turnTo(Math.toRadians(54))
				.build();

		Action line5 = drive.actionBuilder(new Pose2d(-34.5, -33.1, Math.toRadians(54)))
				.strafeTo(new Vector2d(13, -17.8))
				.turnTo(Math.toRadians(-90))
				.build();

		Action line6 = drive.actionBuilder(new Pose2d(13, -17.8, Math.toRadians(-90)))
				.strafeTo(new Vector2d(12, -47.3))
				.build();

		// ---------- Auto ----------
		Actions.runBlocking(new SequentialAction(
				line1,
				shootN(3),

				line2,
				intakeOn(),
				line3,
				intakeOff(),

				line4,
				shootN(3),

				line5,
				intakeOn(),
				line6,
				intakeOff()
		));
	}


	private Action intakeOn() {
		return new InstantAction(() -> intake.setPower(-1));
	}

	private Action intakeOff() {
		return new InstantAction(() -> intake.setPower(0));
	}

	private Action settle() {
		return new SequentialAction(
				new InstantAction(() ->
						launcher.setPosition(LaunchConstants.settledPos)),
				new SleepAction(LaunchConstants.settlingTime)
		);
	}

	private Action loadBall() {
		return new SequentialAction(
				new InstantAction(() ->
						stage2.setPosition(LaunchConstants.stage2Push)),
				new SleepAction(LaunchConstants.pushTime),
				new InstantAction(() ->
						launcher.setPosition(LaunchConstants.settledPos)),
				new SleepAction(LaunchConstants.settlingTime)
		);
	}

	private Action launcherOn() {
		return new SequentialAction(
				new InstantAction(() -> {
					launcherLeft.setVelocity(-500);
					launcherRight.setVelocity(500);
				}),
				new SleepAction(LaunchConstants.warmupTime),
				new InstantAction(() -> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),
				new SleepAction(LaunchConstants.warmupTime)
		);
	}

	private Action launcherOff() {
		return new InstantAction(() -> {
			launcherLeft.setVelocity(0);
			launcherRight.setVelocity(0);
			stage2.setPosition(LaunchConstants.stage2Start);
		});
	}

	private Action pushLauncher(boolean lastShot) {
		return new SequentialAction(
				new InstantAction(() -> {
						launcher.setPosition(LaunchConstants.launchedPos);
						stage2.setPosition(LaunchConstants.stage2Start);}),
				new SleepAction(LaunchConstants.launchTime),
				new InstantAction(() ->
						launcher.setPosition(LaunchConstants.unlaunchedPos)),
				new SleepAction(LaunchConstants.launchTime)
		);
	}

	private Action shootN(int count) {
		List<Action> actions = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			if (i >= 1) {
				actions.add(loadBall());
			}
			actions.add(settle());
			actions.add(launcherOn());
			actions.add(pushLauncher(i == count - 1));
			actions.add(launcherOff());
		}

		return new SequentialAction(actions);
	}
}
