package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Config
@Autonomous(name = "! seth auto", group = "! Auto")
public class SethAuto extends LinearOpMode {

	public static double unlaunchedPos = 0.35;
	public static double settledPos = 0.6;
	public static double launchedPos = 1;
	public static double launchPower = 0.9;

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

		TrajectoryActionBuilder backUp = drive.actionBuilder(new Pose2d(0,0,Math.toDegrees(0)))
				.strafeTo(new Vector2d(0, -10));


		Actions.runBlocking(new SequentialAction(
				        backUp.build(),launch2Artifacts
		));
	}
}