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
@Autonomous(name = "! seth auto (blue)", group = "! Auto")
public class SethAutoBlue extends LinearOpMode {

	public static double turnAmount = 45;

	double deltaX = -LaunchConstants.launchDistance;
	double deltaX2 = -LaunchConstants.secondaryDistance;

	public static double deltaY = 0;

	@Override
	public void runOpMode() {

		MultipleTelemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

		// pinpoint = "launchLeft" encoder

		Pose2d startPos = new Pose2d(new Vector2d(0,0), Math.toRadians(0));

		MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);
		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");
		Servo launcher = hardwareMap.get(Servo.class,"launcher");
		Servo stage2 = hardwareMap.get(Servo.class,"stage2");
		DcMotorEx launcherLeft = hardwareMap.get(DcMotorEx.class,"launchLeft");
		DcMotorEx launcherRight = hardwareMap.get(DcMotorEx.class,"launchRight");
		launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		launcherLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		launcherRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


//		launcher.setPosition(LaunchConstants.unlaunchedPos);
//		stage2.setPosition(stage2Start);
		launcher.setPosition(LaunchConstants.settledPos);
		stage2.setPosition(LaunchConstants.stage2Start);
		waitForStart();


		SequentialAction loadBall = new SequentialAction(
				new InstantAction(()-> {
					intake.setPower(-1);
				}),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Push);
				}), new SleepAction(LaunchConstants.pushTime),
				new InstantAction(()-> {
					intake.setPower(0);
				}),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Start);
					launcher.setPosition(LaunchConstants.settledPos);
				}),new SleepAction(LaunchConstants.settlingTime));
		SequentialAction loadBall2 = new SequentialAction(
				new InstantAction(()-> {
					intake.setPower(-1);
				}),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Push);
				}), new SleepAction(LaunchConstants.pushTime),
				new InstantAction(()-> {
					intake.setPower(0);
				}),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Start);
					launcher.setPosition(LaunchConstants.settledPos);
				}),new SleepAction(LaunchConstants.settlingTime));

		SequentialAction pushLauncher = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(0.5),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(0.5));
		SequentialAction pushLauncher2 = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(0.5),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(0.5));
		SequentialAction pushLauncher3 = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(0.5),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(0.5));
		SequentialAction pushLauncher4 = new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(0.5),
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}),new SleepAction(0.5));

		SequentialAction launcherOn = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));
		SequentialAction launcherOn2 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));;
		SequentialAction launcherOn3 = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}),new SleepAction(LaunchConstants.warmupTime));
		SequentialAction launcherOn4 = new SequentialAction(
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
				}));

		SequentialAction settle = new SequentialAction(new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.settledPos);
				})),new SleepAction(LaunchConstants.settlingTime));
		SequentialAction settle2 = new SequentialAction(new SequentialAction(
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.settledPos);
				})),new SleepAction(LaunchConstants.settlingTime));

//		SequentialAction launch2Artifacts = new SequentialAction(
//				launcherOn,
//				new SleepAction(LaunchConstants.warmupTime),
//				pushLauncher,
//				new SleepAction(LaunchConstants.settlingTime),
//				loadBall,
//				new SleepAction(LaunchConstants.warmupTime),
//				pushLauncher,
//				launcherOff
//		);
		SequentialAction launch2Artifacts = new SequentialAction(
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



		TrajectoryActionBuilder backUp = drive.actionBuilder(startPos)
				.strafeTo(new Vector2d(deltaX, deltaY));

		TrajectoryActionBuilder backUpAgain = backUp.fresh()
				.strafeTo(new Vector2d(deltaX2, 0));

		TrajectoryActionBuilder turn = backUpAgain.fresh()
				.turn(Math.toRadians(turnAmount));

		TrajectoryActionBuilder leave = turn.fresh()
				.lineToX(LaunchConstants.endScoot);


		Actions.runBlocking(new SequentialAction(
				        backUp.build(),launch2Artifacts, backUpAgain.build(), turn.build(), leave.build()
		));
	}
}