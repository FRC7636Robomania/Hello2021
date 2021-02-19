// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.senior_high_two.chassis.ControlDrivetrain;
import frc.robot.subsystems.senior_high_two.chassis.DrivetrainBase;
import frc.robot.subsystems.senior_high_two.chassis.trajectory.TrajectoryDrivetrain;
import frc.robot.subsystems.senior_high_two.chassis.trajectory.TrajectorySystem;

public class Move extends CommandBase {
  /** Creates a new Move. */
  ControlDrivetrain drive;
  public Move(ControlDrivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drive = drivetrain;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive.curvatureDrive(-0.25, 0, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
