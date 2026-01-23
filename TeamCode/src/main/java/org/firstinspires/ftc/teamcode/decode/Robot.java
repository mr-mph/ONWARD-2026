package org.firstinspires.ftc.teamcode.decode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Robot {
	public Drive drive;
	public Launcher launcher;
	public Transfer transfer;

	public Robot(HardwareMap hardwareMap, Pose2d startPose) {
		this.drive = new Drive(hardwareMap, startPose);
		this.launcher = new Launcher(hardwareMap);
		this.transfer = new Transfer(hardwareMap);
	}

	public void sendTelemetry(Telemetry telemetry) {
		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		if (drive.initialized) {
			telemetry.addData("x", drive.mecanumDrive.localizer.getPose().position.x);
			telemetry.addData("y", drive.mecanumDrive.localizer.getPose().position.y);
			telemetry.addData("heading (deg)", Math.toDegrees(drive.mecanumDrive.localizer.getPose().heading.toDouble()));
			telemetry.addData("launcher launching (up?)",launcher.launching);
			telemetry.addData("pusher pushing (out?)",launcher.pushing);
			telemetry.addData("multiplying?",launcher.isMultiplying);

			TelemetryPacket packet = new TelemetryPacket();
			packet.fieldOverlay().setStroke("#3F51B5");
			Drawing.drawRobot(packet.fieldOverlay(), drive.mecanumDrive.localizer.getPose());
			FtcDashboard.getInstance().sendTelemetryPacket(packet);
		}

		telemetry.update();

	}
}
