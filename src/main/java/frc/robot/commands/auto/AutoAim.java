// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.senior_high_two.Racker;
import frc.robot.subsystems.senior_high_two.Tower;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoAim extends SequentialCommandGroup {
  /** Creates a new AutoAim. */
  public AutoAim(Tower tower, Racker rack) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new RunCommand(()->tower.aimming(), tower).withTimeout(0.8));
    addCommands(new InstantCommand(()->tower.towerStop(), tower));
    addCommands(new RunCommand(()->rack.PortDistance(), rack).withTimeout(1.0));
    addCommands(new InstantCommand(()->rack.rackerstop(), rack));
  }
}