// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.Path;
import frc.robot.commands.Arm_motion;
import frc.robot.commands.stage_1;
import frc.robot.commands.stage_2;
import frc.robot.commands.stage_3;
import frc.robot.commands.auto.TrajectoryCommand.OutputMode;
import frc.robot.subsystems.senior_high_one.Arm;
import frc.robot.subsystems.senior_high_one.Conveyor;
import frc.robot.subsystems.senior_high_one.Intake;
import frc.robot.subsystems.senior_high_one.Wing;
import frc.robot.subsystems.senior_high_two.Racker;
import frc.robot.subsystems.senior_high_two.Shooter;
import frc.robot.subsystems.senior_high_two.Tower;
import frc.robot.subsystems.senior_high_two.chassis.ControlDrivetrain;
import frc.robot.subsystems.senior_high_two.chassis.DrivetrainBase;
import frc.robot.subsystems.senior_high_two.chassis.trajectory.TrajectoryFactory;
import frc.robot.subsystems.senior_high_two.chassis.trajectory.TrajectorySystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LeftUp extends SequentialCommandGroup {
  /** Creates a new LeftUp. */
  public LeftUp(ControlDrivetrain base, TrajectorySystem drivetrain, Racker rack, Tower tower, Intake intake, Wing wing, Shooter shooter, Conveyor conveyor, Arm arm) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    // addCommands(new AutoAim(tower, rack)); // aim
    addCommands(new InstantCommand(()->intake.slowForward(), intake));
    addCommands(new InstantCommand(()->wing.forward(), wing));

    addCommands(new Shooting(shooter, conveyor).withTimeout(5));
    // // addCommands(new Arm_motion(arm).withTimeout(1.5));
    addCommands(new InstantCommand(()->intake.stop(), intake));
    addCommands(new InstantCommand(()->wing.stop(), wing)); 

    addCommands(new Move(base).withTimeout(1.0));
    // addCommands(new stage_1(intake, wing).withTimeout(7));
    // addCommands(TrajectoryCommand.build(TrajectoryFactory.getTrajectory(Path.LeftUp), drivetrain, OutputMode.VOLTAGE, drivetrain));
  }
}