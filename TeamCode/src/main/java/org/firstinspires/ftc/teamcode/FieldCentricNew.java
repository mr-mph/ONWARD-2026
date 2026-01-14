package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive.PARAMS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.ftc.LazyHardwareMapImu;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Config
@TeleOp(name = "!Field centric main teleop by Seth", group = "! Teleop")
public class FieldCentricNew extends LinearOpMode {

	public static boolean launching = false;
	public static boolean pushing = false;
	public static boolean intaking = false;

	boolean isMultiplying = false;
	public static double powerMultiplier = 1450;


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

		launcherLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		launcherRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		IMU imu = hardwareMap.get(IMU.class, "imu");

		IMU.Parameters parameters = new IMU.Parameters(
				new RevHubOrientationOnRobot(PARAMS.logoFacingDirection, PARAMS.usbFacingDirection)
		);
		imu.initialize(parameters);
		imu.resetYaw();

		launcher.setPosition(LaunchConstants.unlaunchedPos);
		stage2.setPosition(LaunchConstants.stage2Start);

		double multiplicity = LaunchConstants.multiplicity;





//		launcher.setPosition(LaunchConstants.unlaunchedPos);
//		stage2.setPosition(LaunchConstants.stage2Start);
		waitForStart();


		while (!isStopRequested()) {

			double driveX = ( gamepad1.left_stick_x + gamepad2.left_stick_x);
			double driveY = (- gamepad1.left_stick_y - gamepad2.left_stick_y);
			double turn = (-gamepad1.right_stick_x - gamepad2.right_stick_x);
//			double heading = drive.localizer.getPose().heading.toDouble();
			double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
			heading -= Math.PI / 2;

			double cosHeading = Math.cos(heading);
			double sinHeading = Math.sin(heading);

			telemetry.addData("measured heading: ", heading);
			telemetry.addData("stick x: ", driveX);
			telemetry.addData("stick y: ", driveY);
			telemetry.addData("stick turn: ", turn);

			Vector2d rotatedInput = new Vector2d(
					-driveX * cosHeading - driveY * sinHeading,
					driveX * sinHeading - driveY * cosHeading
			);

			drive.setDrivePowers(
					new PoseVelocity2d(rotatedInput, turn)
			);

			drive.updatePoseEstimate();

			intaking = gamepad1.left_stick_button || gamepad2.left_stick_button || gamepad1.left_trigger > 0.5 || gamepad2.left_trigger > 0.5;
			intake.setPower(intaking ? -1 : 0);

			if (gamepad1.right_bumper || gamepad2.right_bumper) {
				launching = true;
			} else {
				launching = false;
			}
			launcher.setPosition(launching ? LaunchConstants.launchedPos : LaunchConstants.unlaunchedPos);

			if (gamepad1.left_bumper || gamepad2.left_bumper) {
				pushing = true;
			} else {
				pushing = false;
			}

			stage2.setPosition(pushing ? LaunchConstants.stage2Push : LaunchConstants.stage2Start);

			if(gamepad1.xWasPressed()) Actions.runBlocking(new SequentialAction(
					new SequentialAction(
							new InstantAction(()-> {
								launcherLeft.setVelocity(LaunchConstants.launchPower);
								launcherRight.setVelocity(-LaunchConstants.launchPower);
							})),
					new SleepAction(LaunchConstants.warmupTime),
					new SequentialAction(
							new InstantAction(()-> {
								launcher.setPosition(LaunchConstants.launchedPos);
							}),
							new SleepAction(1),
							new InstantAction(()-> {
								launcher.setPosition(LaunchConstants.unlaunchedPos);
							})),
					// 2nd ball
//					new SleepAction(LaunchConstants.settlingTime),
					new SequentialAction(
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
//								stage2.setPosition(LaunchConstants.stage2Start);
//								launcher.setPosition(LaunchConstants.settledPos);
							}),new SleepAction(LaunchConstants.settlingTime)),
					new SequentialAction(
							new InstantAction(()-> {
								stage2.setPosition(LaunchConstants.stage2Start); // new
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
			));

			launcherLeft.setVelocity(isMultiplying ? (gamepad1.right_trigger+gamepad2.right_trigger) * powerMultiplier * multiplicity : (gamepad1.right_trigger+gamepad2.right_trigger) * powerMultiplier);
			launcherRight.setVelocity(isMultiplying ? -(gamepad1.right_trigger+gamepad2.right_trigger) * powerMultiplier * multiplicity : -(gamepad1.right_trigger+gamepad2.right_trigger) * powerMultiplier);

			isMultiplying = gamepad1.a || gamepad2.a;

			telemetry.addData("launcher power",gamepad1.right_trigger);
			telemetry.addData("launcher push speed",gamepad1.left_trigger);

			telemetry.addData("launcher launching (up?)",launching);
			telemetry.addData("pusher pushing (out?)",pushing);
			telemetry.addData("multiplying?",isMultiplying);


			telemetry.update();
		}
	}

}