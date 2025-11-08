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

	public static double deltaX = -35;
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
		launcher.setPosition(LaunchConstants.unlaunchedPos);
		stage2.setPosition(LaunchConstants.stage2Start);
		waitForStart();


		SequentialAction loadBall = new SequentialAction(
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Push);
				}), new SleepAction(1),
				new InstantAction(()-> {
					stage2.setPosition(LaunchConstants.stage2Start);
					launcher.setPosition(LaunchConstants.settledPos);
				}));

		SequentialAction pushLauncher = new SequentialAction(
		new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.launchedPos);
				}),
				new SleepAction(1),
				new InstantAction(()-> {
					launcher.setPosition(LaunchConstants.unlaunchedPos);
				}));

		SequentialAction launcherOn = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(LaunchConstants.launchPower);
					launcherRight.setVelocity(-LaunchConstants.launchPower);
				}));

		SequentialAction launcherOff = new SequentialAction(
				new InstantAction(()-> {
					launcherLeft.setVelocity(0);
					launcherRight.setVelocity(0);
				}));

//		SequentialAction launch2Artifacts = new SequentialAction(
//				launcherOn,
//				new SleepAction(LaunchConstants.WAIT1),
//				pushLauncher,
//				new SleepAction(LaunchConstants.WAIT2),
//				loadBall,
//				new SleepAction(LaunchConstants.WAIT3),
//				pushLauncher,
//				launcherOff
//		);
		SequentialAction launch2Artifacts = new SequentialAction(
				new SequentialAction(
						new InstantAction(()-> {
							launcherLeft.setVelocity(LaunchConstants.launchPower);
							launcherRight.setVelocity(-LaunchConstants.launchPower);
						})),
				new SleepAction(LaunchConstants.WAIT1),
				new SequentialAction(
						new InstantAction(()-> {
							launcher.setPosition(LaunchConstants.launchedPos);
						}),
						new SleepAction(1),
						new InstantAction(()-> {
							launcher.setPosition(LaunchConstants.unlaunchedPos);
						})),
				new SleepAction(LaunchConstants.WAIT2),
				new SequentialAction(
						new InstantAction(()-> {
							stage2.setPosition(LaunchConstants.stage2Push);
						}), new SleepAction(1),
						new InstantAction(()-> {
							stage2.setPosition(LaunchConstants.stage2Start);
							launcher.setPosition(LaunchConstants.settledPos);
						})),
				new SleepAction(LaunchConstants.WAIT3),
				new SequentialAction(
						new InstantAction(()-> {
							launcher.setPosition(LaunchConstants.launchedPos);
						}),
						new SleepAction(1),
						new InstantAction(()-> {
							launcher.setPosition(LaunchConstants.unlaunchedPos);
						})),
				new SequentialAction(
						new InstantAction(()-> {
							launcherLeft.setVelocity(0);
							launcherRight.setVelocity(0);
						}))
		);



		TrajectoryActionBuilder backUp = drive.actionBuilder(startPos)
				.strafeTo(new Vector2d(deltaX, deltaY));

		TrajectoryActionBuilder turn = backUp.fresh()
				.turn(Math.toRadians(turnAmount));


		Actions.runBlocking(new SequentialAction(
				        backUp.build(),launch2Artifacts, turn.build()
		));
	}
}