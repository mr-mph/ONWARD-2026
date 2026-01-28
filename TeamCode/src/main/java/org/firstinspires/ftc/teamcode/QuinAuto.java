package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Config
@Autonomous(name = "! quin auto", group = "! Auto")
public class QuinAuto extends LinearOpMode {

	public static double unlaunchedPos = 0.35;
	public static double settledPos = 0.6;
	public static double launchedPos = 1;

	public static double stage2Start = 0.6;
	public static double stage2Push = 0.2;

	public static double WAIT1 = 1;
	public static double WAIT2 = 1;
	public static double WAIT3 = 0;

	@Override
	public void runOpMode() {

		MultipleTelemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

		// pinpoint = "launchLeft" encoder

		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");
		Servo launcher = hardwareMap.get(Servo.class,"launcher");
		Servo stage2 = hardwareMap.get(Servo.class,"stage2");
		DcMotorEx launcherLeft = hardwareMap.get(DcMotorEx.class,"launchLeft");
		DcMotorEx launcherRight = hardwareMap.get(DcMotorEx.class,"launchRight");
		launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		Pose2d startPos = new Pose2d(-51, -48, Math.toRadians(54));
		MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);


//		launcher.setPosition(unlaunchedPos);
//		stage2.setPosition(stage2Start);
		launcher.setPosition(unlaunchedPos);
		stage2.setPosition(stage2Start);
		waitForStart();


		SequentialAction loadBall = new SequentialAction(
				new InstantAction(()-> {
//					intake.setPower(-1);
				}),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Push);
				}), new SleepAction(LaunchConstants.pushTime),
				new InstantAction(()-> {
					intake.setPower(0);
				}),
				new InstantAction(()-> {
//					stage2.setPosition(LaunchConstants.stage2Start);
					launcher.setPosition(LaunchConstants.settledPos);
				}),new SleepAction(LaunchConstants.settlingTime));
		SequentialAction loadBall2 = new SequentialAction(
				new InstantAction(()-> {
//					intake.setPower(-1);
				}),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Push);
				}), new SleepAction(LaunchConstants.pushTime),
				new InstantAction(()-> {
					intake.setPower(0);
				}),
				new InstantAction(()-> {
//					stage2.setPosition(LaunchConstants.stage2Start);
					launcher.setPosition(LaunchConstants.settledPos);
				}),new SleepAction(LaunchConstants.settlingTime));

		SequentialAction pushLauncher = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(LaunchConstants.launchTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(LaunchConstants.launchTime));
		SequentialAction pushLauncher2 = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(LaunchConstants.launchTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(LaunchConstants.launchTime));
		SequentialAction pushLauncher3 = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
					stage2.setPosition(LaunchConstants.stage2Start);
				}),
				new SleepAction(LaunchConstants.launchTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);

				}),new SleepAction(LaunchConstants.launchTime));
		SequentialAction pushLauncher4 = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(LaunchConstants.launchTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(LaunchConstants.launchTime));

		SequentialAction launcherOn = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(-500);
					launcherRight.setVelocity(500);
				}),new SleepAction(LaunchConstants.warmupTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));
		SequentialAction launcherOn2 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(-500);
					launcherRight.setVelocity(500);
				}),new SleepAction(LaunchConstants.warmupTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));
		SequentialAction launcherOn3 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(-500);
					launcherRight.setVelocity(500);
				}),new SleepAction(LaunchConstants.warmupTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));
		SequentialAction launcherOn4 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(-500);
					launcherRight.setVelocity(500);
				}),new SleepAction(LaunchConstants.warmupTime),
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));


		SequentialAction launcherOff = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}));
		SequentialAction launcherOff2 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}));
		SequentialAction launcherOff3 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}));
		SequentialAction launcherOff4 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
					stage2.setPosition(LaunchConstants.stage2Start);
				}));

		SequentialAction settle = new SequentialAction(new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.settledPos);
				})),new SleepAction(LaunchConstants.settlingTime));
		SequentialAction settle2 = new SequentialAction(new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.settledPos);
				})),new SleepAction(LaunchConstants.settlingTime));

		SequentialAction launch2Artifacts1 = new SequentialAction(
				settle,
				launcherOn,
				pushLauncher,
				launcherOff,
				// 2nd
				settle2,
				launcherOn2,
				pushLauncher2,
				launcherOff2,
				// 3rd
				loadBall,
				launcherOn3,
				pushLauncher3,
				launcherOff3,
				// 4th
				loadBall2,
				launcherOn4,
				pushLauncher4,
				launcherOff4
		);
		SequentialAction launch2Artifacts2 = new SequentialAction(
				settle,
				launcherOn,
				pushLauncher,
				launcherOff,
				// 2nd
				settle2,
				launcherOn2,
				pushLauncher2,
				launcherOff2,
				// 3rd
				loadBall,
				launcherOn3,
				pushLauncher3,
				launcherOff3,
				// 4th
				loadBall2,
				launcherOn4,
				pushLauncher4,
				launcherOff4
		);

		TrajectoryActionBuilder line1 = drive.actionBuilder(startPos)
				.strafeTo(new Vector2d(-34.5, -33.1))
				.endTrajectory();
		TrajectoryActionBuilder line2 = line1.fresh()
				.strafeTo(new Vector2d(-17, -17.8))
				.turnTo(Math.toRadians(-90))
				.endTrajectory();
		TrajectoryActionBuilder line3 = line2.fresh()
				.strafeTo(new Vector2d(-17, -47.3))
				.endTrajectory();
		TrajectoryActionBuilder line4 = line3.fresh()
				.strafeTo(new Vector2d(-34.5, -33.1))
				.turnTo(Math.toRadians(54))
				.endTrajectory();
		TrajectoryActionBuilder line5 = line4.fresh()
				.strafeTo(new Vector2d(13, -17.8))
				.turnTo(Math.toRadians(-90))
				.endTrajectory();
		TrajectoryActionBuilder line6 = line5.fresh()
				.strafeTo(new Vector2d(12, -47.3))
				.endTrajectory();

		Actions.runBlocking(new SequentialAction(
				line1.build(),
				launch2Artifacts1,
				line2.build(),
				new InstantAction(() -> {intake.setPower(-1);}),
				line3.build(),
				new InstantAction(() -> {intake.setPower(0);}),
				line4.build(),
				launch2Artifacts2,
				line5.build(),
				new InstantAction(() -> {intake.setPower(-1);}),
				line6.build(),
				new InstantAction(() -> {intake.setPower(0);})


		));
	}
}