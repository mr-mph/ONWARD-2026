package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp(name = "!Main teleop by Seth", group = "! Teleop")
public class MainTeleop extends LinearOpMode {

	boolean launching = false;



	@Override
	public void runOpMode() {

		MultipleTelemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

		MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(18,-63),-90));
		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");
		Servo launcher = hardwareMap.get(Servo.class,"launcher");
		DcMotorEx launcherLeft = hardwareMap.get(DcMotorEx.class,"launchLeft");
		DcMotorEx launcherRight = hardwareMap.get(DcMotorEx.class,"launchRight");


		waitForStart();
		intake.setPower(-1);
//		launcherLeft.setPower(1);
//		launcherRight.setPower(-1);

		while (!isStopRequested()) {

			drive.setDrivePowers(
					new PoseVelocity2d(new Vector2d(
							(-gamepad1.left_stick_y - gamepad2.left_stick_y),
							(-gamepad1.left_stick_x - gamepad2.left_stick_x)),
							(-gamepad1.right_stick_x - gamepad2.right_stick_x)
					));

			drive.updatePoseEstimate();

			if (gamepad1.dpad_left || gamepad2.dpad_left) {
				intake.setPower(1);
			} else if (gamepad1.dpad_right || gamepad2.dpad_right) {
				intake.setPower(-1);
			} else if (gamepad1.dpad_down || gamepad2.dpad_down) {
				intake.setPower(0);
			}
			
			if (gamepad1.right_bumper || gamepad2.right_bumper) {
				if (launching) {
					launcher.setPosition(0);
					launching = false;
				} else {
					launcher.setPosition(1);
					launching = true;
				}
			}


			launcherLeft.setPower(gamepad1.right_trigger+gamepad2.right_trigger);
			launcherRight.setPower(-gamepad1.right_trigger-gamepad2.right_trigger);

			telemetry.addData("power",gamepad1.right_trigger);
			telemetry.update();


		}
	}
}