package org.firstinspires.ftc.teamcode.decode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;


@Config
public class Drive {

	public HardwareMap hardwareMap;
	public boolean initialized;
	public org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive mecanumDrive;
	public com.qualcomm.robotcore.hardware.DcMotorEx intake;

	public Drive(HardwareMap hardwareMap, com.acmerobotics.roadrunner.Pose2d startPose) {
		this.hardwareMap = hardwareMap;
		mecanumDrive = new org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive(hardwareMap, startPose);
		intake = hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotorEx.class, "intake");

		initialized = true;

	}
}
