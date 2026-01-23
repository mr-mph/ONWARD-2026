package org.firstinspires.ftc.teamcode.decode.teleop;
import static org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive.PARAMS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.decode.Robot;

@Disabled
@Config
@TeleOp(name = "!Old field centric main teleop by Seth", group = "! Teleop")
public class FieldCentricTeleop extends LinearOpMode {

    Robot robot;
    public boolean intaking = false;

    public double startPosX = 18;
    public double startPosY = -63;
    public double startPosHeading = -90;

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap, new Pose2d(new Vector2d(startPosX, startPosY), startPosHeading));

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(PARAMS.logoFacingDirection, PARAMS.usbFacingDirection)
        );
        imu.initialize(parameters);
        imu.resetYaw();

        robot.launcher.launcher.setPosition(robot.launcher.unlaunchedPos);
        robot.transfer.stage2.setPosition(robot.transfer.stage2Start);
        double multiplicity = robot.launcher.multiplicity;

        waitForStart();

        while (!isStopRequested()) {
            double driveX = (gamepad1.left_stick_x + gamepad2.left_stick_x);
            double driveY = (-gamepad1.left_stick_y - gamepad2.left_stick_y);
            double turn = (gamepad1.right_stick_x + gamepad2.right_stick_x);
            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            heading -= Math.PI / 2;

            double cosHeading = Math.cos(heading);
            double sinHeading = Math.sin(heading);

            telemetry.addData("measured heading: ", heading);
            telemetry.addData("stick x: ", driveX);
            telemetry.addData("stick y: ", driveY);
            telemetry.addData("stick turn: ", turn);

            Vector2d rotatedInput = new Vector2d(
                    -driveX * cosHeading - driveY * sinHeading,
                    driveX * sinHeading - driveY * cosHeading
            );

            robot.drive.mecanumDrive.setDrivePowers(
                    new PoseVelocity2d(rotatedInput, turn)
            );
            robot.drive.mecanumDrive.updatePoseEstimate();

            if (gamepad1.left_stick_button || gamepad2.left_stick_button || gamepad1.left_trigger > 0.5 || gamepad2.left_trigger > 0.5) {
                intaking = !intaking;
                while (gamepad1.left_stick_button || gamepad2.left_stick_button || gamepad1.left_trigger > 0.5 || gamepad2.left_trigger > 0.5) {}
            }
            robot.drive.intake.setPower(intaking ? -1 : 0);

            if (gamepad1.right_bumper || gamepad2.right_bumper) {
                robot.launcher.launching = true;
            } else {
                robot.launcher.launching = false;
            }
            robot.launcher.launcher.setPosition(robot.launcher.launching ? robot.launcher.launchedPos : robot.launcher.unlaunchedPos);

            if (gamepad1.left_bumper || gamepad2.left_bumper) {
                robot.launcher.pushing = true;
            } else {
                robot.launcher.pushing = false;
            }
            robot.transfer.stage2.setPosition(robot.launcher.pushing ? robot.transfer.stage2Push : robot.transfer.stage2Start);

            if (gamepad1.xWasPressed()) Actions.runBlocking(new SequentialAction(
                    new SequentialAction(
                            new InstantAction(() -> {
                                robot.launcher.launcherLeft.setVelocity(robot.launcher.launchPower);
                                robot.launcher.launcherRight.setVelocity(-robot.launcher.launchPower);
                            })),
                    new SleepAction(robot.launcher.warmupTime),
                    new SequentialAction(
                            new InstantAction(() -> {
                                robot.launcher.launcher.setPosition(robot.launcher.launchedPos);
                            }),
                            new SleepAction(1),
                            new InstantAction(() -> {
                                robot.launcher.launcher.setPosition(robot.launcher.unlaunchedPos);
                            })),
                    new SequentialAction(
                            new InstantAction(() -> {
                                robot.drive.intake.setPower(-1);
                            }),
                            new InstantAction(() -> {
                                robot.transfer.stage2.setPosition(robot.transfer.stage2Push);
                            }), new SleepAction(robot.transfer.pushTime),
                            new InstantAction(() -> {
                                robot.drive.intake.setPower(0);
                            }),
                            new InstantAction(() -> {
                            }), new SleepAction(robot.launcher.settlingTime)),
                    new SequentialAction(
                            new InstantAction(() -> {
                                robot.transfer.stage2.setPosition(robot.transfer.stage2Start);
                                robot.launcher.launcher.setPosition(robot.launcher.launchedPos);
                            }),
                            new SleepAction(1),
                            new InstantAction(() -> {
                                robot.launcher.launcher.setPosition(robot.launcher.unlaunchedPos);
                            })),
                    new SequentialAction(
                            new InstantAction(() -> {
                                robot.launcher.launcherLeft.setVelocity(0);
                                robot.launcher.launcherRight.setVelocity(0);
                            }))
            ));

            robot.launcher.launcherLeft.setVelocity(robot.launcher.isMultiplying ? (gamepad1.right_trigger + gamepad2.right_trigger) * robot.launcher.powerMultiplier * multiplicity : (gamepad1.right_trigger + gamepad2.right_trigger) * robot.launcher.powerMultiplier);
            robot.launcher.launcherRight.setVelocity(robot.launcher.isMultiplying ? -(gamepad1.right_trigger + gamepad2.right_trigger) * robot.launcher.powerMultiplier * multiplicity : -(gamepad1.right_trigger + gamepad2.right_trigger) * robot.launcher.powerMultiplier);

            robot.launcher.isMultiplying = gamepad1.a || gamepad2.a;

            telemetry.update();
        }
    }
}