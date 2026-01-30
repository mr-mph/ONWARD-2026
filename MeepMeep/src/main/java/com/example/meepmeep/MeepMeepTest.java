package com.example.meepmeep;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.ArrayList;
import java.util.List;

public class MeepMeepTest {
	public static void main(String[] args) {
		Pose2d startPos = new Pose2d(-51, -48, Math.toRadians(54));

		MeepMeep meepMeep = new MeepMeep(800);

		RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
				// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
				.setConstraints(65, 60, Math.toRadians(180), Math.toRadians(180), 11)
				.setDimensions(14.75,17)
				.build();

		DriveShim drive = myBot.getDrive();

		// begin pathing

		TrajectoryActionBuilder line1 = drive.actionBuilder(startPos) // backup to shoot
				.strafeTo(new Vector2d(-42,-39))
				.endTrajectory();

		TrajectoryActionBuilder line2 = line1.fresh() // backup to not knock balls
				.strafeTo(new Vector2d(-43.7,-28.8))
				.endTrajectory();

		TrajectoryActionBuilder line3 = line2.fresh() // primary
				.strafeToLinearHeading(new Vector2d(-13, -25), Math.toRadians(-90))
				.endTrajectory();

		TrajectoryActionBuilder line4 = line3.fresh() // go through
				.strafeTo(new Vector2d(-13, -47.3))
				.endTrajectory();

		TrajectoryActionBuilder line5 = line4.fresh() // go to shoot pos
				.strafeToLinearHeading(new Vector2d(-42, -39), Math.toRadians(54))
				.endTrajectory();

		TrajectoryActionBuilder line6 = line5.fresh() // primary
				.strafeToLinearHeading(new Vector2d(11, -25.8), Math.toRadians(-90))
				.endTrajectory();

		TrajectoryActionBuilder line7 = line6.fresh() // go through
				.strafeTo(new Vector2d(11, -47.3))
				.endTrajectory();

		// ---------- Auto ----------
		myBot.runAction(new SequentialAction(
				line1.build(),

				line2.build(),
				line3.build(),
				line4.build(),

				line5.build(),

				line6.build(),
				line7.build()
		));
		meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
				.setDarkMode(true)
				.setBackgroundAlpha(0.95f)
				.addEntity(myBot)
				.start();
	}
}