package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive.PARAMS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
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
	public static double unlaunchedPos = 0.35;
	public static double launchedPos = 1;
	public static double launcherMultiplier = 0.5;
	public static double launcherDefault = -0.1;


	public static boolean pushing = false;
	public static boolean intaking = false;


	public static double stage2Start = 0.6;
	public static double stage2Push = 0.2;


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
		IMU imu = hardwareMap.get(IMU.class, "imu");

		IMU.Parameters parameters = new IMU.Parameters(
				new RevHubOrientationOnRobot(PARAMS.logoFacingDirection, PARAMS.usbFacingDirection)
		);
		imu.initialize(parameters);
		imu.resetYaw();



//		launcher.setPosition(unlaunchedPos);
//		stage2.setPosition(stage2Start);
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
					driveX * cosHeading + driveY * sinHeading,
					-driveX * sinHeading + driveY * cosHeading
			);

			drive.setDrivePowers(
					new PoseVelocity2d(rotatedInput, turn)
			);

			drive.updatePoseEstimate();

//			if (gamepad1.dpad_left || gamepad2.dpad_left) {
//				intake.setPower(1);
//			} else if (gamepad1.dpad_right || gamepad2.dpad_right) {
//				intake.setPower(-1);
//			} else if (gamepad1.dpad_down || gamepad2.dpad_down) {
//				intake.setPower(0);
//			}

			intaking = gamepad1.right_trigger + gamepad2.right_trigger > 0.5;
			intake.setPower(intaking ? -1 : 0);

			if (gamepad1.right_bumper || gamepad2.right_bumper) {
				launching = true;
			} else {
				launching = false;
			}
			launcher.setPosition(launching ? launchedPos : unlaunchedPos);

			if (gamepad1.left_bumper || gamepad2.left_bumper) {
				pushing = true;
			} else {
				pushing = false;
			}

			stage2.setPosition(pushing ? stage2Push : stage2Start);

			launcherLeft.setPower(gamepad1.right_trigger+gamepad2.right_trigger);
			launcherRight.setPower(-gamepad1.right_trigger-gamepad2.right_trigger);

			telemetry.addData("launcher power",gamepad1.right_trigger);
			telemetry.addData("launcher push speed",gamepad1.left_trigger);

			telemetry.addData("launcher launching (up?)",launching);
			telemetry.addData("pusher pushing (out?)",pushing);


			telemetry.update();


		}
	}

}