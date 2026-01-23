package org.firstinspires.ftc.teamcode.archive;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Disabled
@TeleOp(name = "!Outtake test by Seth", group = "! Teleop")
public class OuttakeTest extends LinearOpMode {



	@Override
	public void runOpMode() {

		MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(18,-63),-90));
		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");
		Servo launcher = hardwareMap.get(Servo.class,"launcher");
		DcMotorEx launcherLeft = hardwareMap.get(DcMotorEx.class,"launchLeft");
		DcMotorEx launcherRight = hardwareMap.get(DcMotorEx.class,"launchRight");



		waitForStart();
//		intake.setPower(-1);

		while (!isStopRequested()) {

//			drive.setDrivePowers(
//					new PoseVelocity2d(new Vector2d(
//							(-gamepad1.left_stick_y - gamepad2.left_stick_y),
//							(-gamepad1.left_stick_x - gamepad2.left_stick_x)),
//							(-gamepad1.right_stick_x - gamepad2.right_stick_x)
//					));
//
//			drive.updatePoseEstimate();

			if (gamepad1.dpad_left || gamepad2.dpad_left) {
				launcher.setPosition(-1);
			} else if (gamepad1.dpad_right || gamepad2.dpad_right) {
				launcher.setPosition(1);
			}

			launcherLeft.setPower(-gamepad1.left_stick_y);
			launcherRight.setPower(gamepad1.left_stick_y);
			telemetry.addData("power",gamepad1.left_stick_y);
			telemetry.update();

//			if (gamepad1.left_bumper|| gamepad2.left_bumper) {
//				launcherLeft.setPower(1);
//			} else if (gamepad1.left_trigger > 0.5  || gamepad2.left_trigger > 0.5) {
//				launcherLeft.setPower(0);
//			}
//
//			if (gamepad1.right_bumper || gamepad2.right_bumper) {
//				launcherRight.setPower(-1);
//			} else if (gamepad1.right_trigger > 0.5  || gamepad2.right_trigger > 0.5) {
//				launcherRight.setPower(0);
//			}


		}
	}
}