//package org.firstinspires.ftc.teamcode;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//
//@TeleOp(name = "! PedroPathing drivetest", group = "Teleop")
//public class PedroTeleop extends OpMode {
//	private Follower follower;
//
//	@Override
//	public void init() {
//		follower = Constants.createFollower(hardwareMap);
//		follower.setStartingPose(new Pose());
//		follower.update();
//	}
//
//	@Override
//	public void start() {
//		follower.update();
//		follower.startTeleopDrive();
//	}
//
//	@Override
//	public void loop() {
//		follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
//		follower.update();
//
//		telemetry.addData("X", follower.getPose().getX());
//		telemetry.addData("Y", follower.getPose().getY());
//		telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));
//		telemetry.update();
//	}
//}
