package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp(name = "!Field centric teleop by Seth", group = "! Teleop")
public class DriveTest3 extends LinearOpMode {



	@Override
	public void runOpMode() {

		MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(18,-63),-90));
		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");

		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

		waitForStart();

		intake.setPower(-1);

		while (!isStopRequested()) {

			double driveX = (gamepad1.left_stick_x + gamepad2.left_stick_x);
			double driveY = (-gamepad1.left_stick_y - gamepad2.left_stick_y);
			double turn = (-gamepad1.right_stick_x - gamepad2.right_stick_x);

			double heading = drive.localizer.getPose().heading.toDouble();
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

			if (gamepad1.dpad_left || gamepad2.dpad_left) {
				intake.setPower(1);
			} else if (gamepad1.dpad_right || gamepad2.dpad_right) {
				intake.setPower(-1);
			} else if (gamepad1.dpad_down || gamepad2.dpad_down) {
				intake.setPower(0);
			}

			telemetry.update();

		}
	}
}