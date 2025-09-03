package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "Example Robot-Centric TeleOp", group = "Examples")
public class TeleOpExample extends OpMode {
	private Follower follower;
	/** Start Pose of our robot. This can be changed or saved from the autonomous period. */
	private final Pose startPose = new Pose(60,96, Math.toRadians(-90));
	private final Pose scorePose = new Pose(16,128, Math.toRadians(-45));
	private boolean prevXPressed;
	private boolean prevYPressed;
	private boolean isFollowingPath;

	/** This method is called once when init is pressed and initializes the follower **/
	@Override
	public void init() {
		// Initializing the follower and setting its starting position.
		follower = Constants.createFollower(hardwareMap);
		follower.setStartingPose(startPose);
	}

	/** This method is called once at the start of the OpMode. **/
	@Override
	public void start() {
		// Calling this method is necessary at the start of your TeleOp OpMode.
		follower.startTeleopDrive();
	}

	/** This is the main loop of the OpMode and runs continuously after pressing play **/
	@Override
	public void loop() {
		/*
		 * A rising edge detector to check if the x button was pressed.
		 * If the x button was pressed, drive the robot back to the basket.
		 */
		if (gamepad1.x && !prevXPressed) {
			// Make sure to stop any paths the follower was following.
			follower.breakFollowing();
			PathChain scoreSample = follower.pathBuilder()
					.addPath(new BezierLine(follower.getPose(), scorePose))
					.setLinearHeadingInterpolation(follower.getPose().getHeading(), scorePose.getHeading())
					.build();
			follower.followPath(scoreSample);
			isFollowingPath = true;
		}

		/*
		 * A rising edge detector to check if the y button was pressed.
		 * If the y button was pressed, relocalize the robot.
		 */
		if (gamepad1.y && !prevYPressed) {
			// Resetting the position of the robot
			follower.setStartingPose(new Pose(9, 111, Math.toRadians(0)));
		}


		if (isFollowingPath && !follower.isBusy()) {
			// Stop following the path so that the driver can retake control of the robot.
			follower.breakFollowing();
			isFollowingPath = false;
		}

		// Update robot movement based on gamepad inputs
		if (!isFollowingPath) {
			/* Update Pedro to move the robot based on:
			 * Forward/Backward Movement: -gamepad1.left_stick_y
			 * Left/Right Movement: -gamepad1.left_stick_x
			 * Turn Left/Right Movement: -gamepad1.right_stick_x
			 * Robot-Centric Mode: true
			 */
			follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
		}
		// Loop robot movement and odometry values
		follower.update();

		// Telemetry Outputs of the Follower
		telemetry.addData("X", follower.getPose().getX());
		telemetry.addData("Y", follower.getPose().getY());
		telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

		// Update Telemetry to the Driver Hub
		telemetry.update();

		// Updating the status of the gamepad buttons
		prevXPressed = gamepad1.x;
		prevYPressed = gamepad1.y;
	}
}
