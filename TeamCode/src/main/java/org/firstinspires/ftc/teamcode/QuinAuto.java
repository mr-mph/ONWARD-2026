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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Config
@Autonomous(name = "! wuin auto", group = "! Auto")
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

		MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(18, -63), -90));
		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");
		Servo launcher = hardwareMap.get(Servo.class,"launcher");
		Servo stage2 = hardwareMap.get(Servo.class,"stage2");
		DcMotorEx launcherLeft = hardwareMap.get(DcMotorEx.class,"launchLeft");
		DcMotorEx launcherRight = hardwareMap.get(DcMotorEx.class,"launchRight");
		launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		Pose2d startPos = new Pose2d(56, 7, 90);

//		launcher.setPosition(unlaunchedPos);
//		stage2.setPosition(stage2Start);
		launcher.setPosition(unlaunchedPos);
		stage2.setPosition(stage2Start);
		waitForStart();


		SequentialAction loadBall = new SequentialAction(
				new InstantAction(()-> {
					stage2.setPosition(stage2Push);
				}), new SleepAction(1),
				new InstantAction(()-> {
					stage2.setPosition(stage2Start);
					launcher.setPosition(settledPos);
				}));

		SequentialAction pushLauncher = new SequentialAction(
		new InstantAction(()-> {
					launcher.setPosition(launchedPos);
				}),
				new SleepAction(1),
				new InstantAction(()-> {
					launcher.setPosition(unlaunchedPos);
				}));

		SequentialAction launcherOn =  new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setPower(1);
					launcherRight.setPower(-1);
				}));

		SequentialAction launcherOff = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setPower(0);
					launcherRight.setPower(0);
				}));

		SequentialAction launch2Artifacts = new SequentialAction(
				launcherOn,
				new SleepAction(WAIT1),
				loadBall,
				new SleepAction(WAIT2),
				pushLauncher,
				new SleepAction(WAIT3),
				launcherOff
		);

		TrajectoryActionBuilder line1 = drive.actionBuilder(startPos)
				.strafeTo(new Vector2d(56, 36))
				.turn(180)
				.endTrajectory();
		TrajectoryActionBuilder line2 = line1.fresh()
				.strafeTo(new Vector2d(14.6, 36))
				.endTrajectory();
		TrajectoryActionBuilder line3 = line2.fresh()
				.strafeTo(new Vector2d(73.3, 63.3))
				.turn(135)
				.endTrajectory();
		TrajectoryActionBuilder line4 = line3.fresh()
				.strafeTo(new Vector2d(54, 100))
				.endTrajectory();
		TrajectoryActionBuilder line5 = line4.fresh()
				.strafeTo(new Vector2d(53.7, 60))
				.turn(180)
				.endTrajectory();
		TrajectoryActionBuilder line6 = line5.fresh()
				.strafeTo(new Vector2d(12.6, 60))
				.endTrajectory();
		TrajectoryActionBuilder line7 = line6.fresh()
				.strafeTo(new Vector2d(65.7, 77.7))
				.turn(135)
				.endTrajectory();
		TrajectoryActionBuilder line8 = line7.fresh()
				.strafeTo(new Vector2d(54, 100))
				.endTrajectory();
		TrajectoryActionBuilder line9 = line8.fresh()
				.strafeTo(new Vector2d(54, 84))
				.turn(180)
				.endTrajectory();
		TrajectoryActionBuilder line10 = line9.fresh()
				.strafeTo(new Vector2d(11.8, 84))
				.endTrajectory();
		TrajectoryActionBuilder line11 = line10.fresh()
				.strafeTo(new Vector2d(54, 100))
				.turn(135)
				.endTrajectory();

		Actions.runBlocking(new SequentialAction(
				line1.build(),
				new InstantAction(() -> {intake.setPower(1);}),
				line2.build(),
				new InstantAction(() -> {intake.setPower(0);}),
				line3.build(),
				line4.build(),
				launch2Artifacts,
				line5.build(),
				new InstantAction(() -> {intake.setPower(1);}),
				line6.build(),
				new InstantAction(() -> {intake.setPower(0);}),
				line7.build(),
				line8.build(),
				launch2Artifacts,
				line9.build(),
				new InstantAction(() -> {intake.setPower(1);}),
				line10.build(),
				new InstantAction(() -> {intake.setPower(0);}),
				line11.build(),
				launch2Artifacts
		));
	}
}