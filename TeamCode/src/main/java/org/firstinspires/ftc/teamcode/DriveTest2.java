package org.firstinspires.ftc.teamcode;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp(name = "!Roadrunner teleop by Seth", group = "! Teleop")
public class DriveTest2 extends LinearOpMode {



	@Override
	public void runOpMode() {

		MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(18,-63),-90));
		DcMotorEx intake = hardwareMap.get(DcMotorEx.class,"intake");


		waitForStart();
		intake.setPower(-1);

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


		}
	}
}