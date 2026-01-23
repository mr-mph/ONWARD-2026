//package org.firstinspires.ftc.teamcode.archive;
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.PoseVelocity2d;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.roadrunner.SimpleMecanumDrive;
//
//@Disabled
//@Config
//@TeleOp(name = "!Nathan teleop", group = "! Teleop")
//public class NathanTest extends LinearOpMode {
//
//
//	@Override
//	public void runOpMode() {
//
//		MultipleTelemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
//
//		// pinpoint = "launchLeft" encoder
//
//		SimpleMecanumDrive drive = new SimpleMecanumDrive(hardwareMap, new Pose2d(new Vector2d(18, -63), -90));
//
//		waitForStart();
//
//
//		while (!isStopRequested()) {
//
//			drive.setDrivePowers(
//					new PoseVelocity2d(new Vector2d(
//							(-gamepad1.left_stick_y - gamepad2.left_stick_y),
//							(- gamepad1.left_stick_x - gamepad2.left_stick_x)),
//							(-gamepad1.right_stick_x - gamepad2.right_stick_x)
//					));
//
//			drive.updatePoseEstimate();
//		}
//	}
//}