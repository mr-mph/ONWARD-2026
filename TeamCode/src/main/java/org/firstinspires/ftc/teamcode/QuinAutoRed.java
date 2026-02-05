package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.ProfileParams;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilderParams;
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
@Autonomous(name = "! quin auto", group = "! Auto")
public class QuinAutoRed extends LinearOpMode {

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


		TrajectoryActionBuilder line1 = drive.actionBuilder(startPos) // backup to shoot
				.strafeTo(new Vector2d(-42,-39))
				.endTrajectory();

		TrajectoryActionBuilder line2 = line1.fresh() // backup to not knock balls
				.strafeTo(new Vector2d(-43.7,2-8.8))
				.endTrajectory();

		TrajectoryActionBuilder line3 = line2.fresh() // primary
				.strafeToLinearHeading(new Vector2d(-13, -25), Math.toRadians(-90))
				.endTrajectory();

		TrajectoryActionBuilder line4 = line3.fresh() // go through
				.strafeTo(new Vector2d(-13, -47.3))
				.endTrajectory();

		TrajectoryActionBuilder line5 = line4.fresh() // go to shoot pos
				.strafeToLinearHeading(new Vector2d(-42, -39), Math.toRadians(54))
				.endTrajectory();

		TrajectoryActionBuilder line6 = line5.fresh() // primary
				.strafeToLinearHeading(new Vector2d(11, -25.8), Math.toRadians(-90))
				.endTrajectory();

		TrajectoryActionBuilder line7 = line6.fresh() // go through
				.strafeTo(new Vector2d(11, -47.3))
				.endTrajectory();

		// ---------- Auto ----------
		Actions.runBlocking(new SequentialAction(
				line1.build(),
				shootN(3),

				line2.build(),
				line3.build(),
				intakeOn(),
				line4.build(),
				intakeOff(),

				line5.build(),
				shootN(3),

				line6.build(),
				intakeOn(),
				line7.build(),
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
						launcher.setPosition(LaunchConstants.launchedPos);}),
				new SleepAction(LaunchConstants.settlingTime),
				new InstantAction(() -> {
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
