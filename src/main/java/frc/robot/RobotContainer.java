// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.*;
import frc.robot.commands.auto.LeftUp;
import frc.robot.commands.auto.TrajectoryCommand;
import frc.robot.subsystems.senior_high_one.*;
import frc.robot.subsystems.senior_high_two.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.senior_high_two.chassis.ControlDrivetrain;
import frc.robot.subsystems.senior_high_two.chassis.trajectory.TrajectoryDrivetrain;
import frc.robot.subsystems.senior_high_two.chassis.trajectory.TrajectoryFactory;

public class RobotContainer {
  private XboxController findHitoABoyfriend = new XboxController(0);
  private Joystick joystick = new Joystick(1);
  private final Intake m_Intake = new Intake();
  private final Arm m_arm = new Arm();
  private Wing m_Wing = new Wing();
  private Limelight m_Limelight = new Limelight();
  private Tower m_tower = new Tower(m_Limelight);
  private Racker m_Racker = new Racker(m_Limelight);
  private Shooter m_Shooter = new Shooter(m_Limelight);
  private Conveyor m_Conveyor = new Conveyor(m_Shooter);
  private ControlDrivetrain controlDrivetrain = new ControlDrivetrain();
  private TrajectoryDrivetrain trajectoryDrivetrain = new TrajectoryDrivetrain();
  private final SendableChooser<Command> chooser = new SendableChooser<Command>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    Status();
    teleop();
    Compressor();
    configureButtonBindings();
  }

  public void chooser(){
    chooser.addOption("LeftUp", new LeftUp(trajectoryDrivetrain, 
                                           m_Racker, m_tower, m_Intake, 
                                           m_Wing, m_Shooter, m_Conveyor, m_arm));
    chooser.setDefaultOption("Null", null);
    chooser.addOption("one", TrajectoryCommand.build(TrajectoryFactory.getTrajectory("output/test.wpilib.json"), trajectoryDrivetrain, TrajectoryCommand.OutputMode.VOLTAGE, trajectoryDrivetrain));
    SmartDashboard.putData(chooser);
  }

  private void configureButtonBindings() {
    new JoystickButton(findHitoABoyfriend, Constants.Button.intake_wing)          .whenHeld(new stage_1(m_Intake,m_Wing));
    new JoystickButton(joystick, Constants.Button.flywheel)             .whenHeld(new stage_2(m_Shooter));
    new JoystickButton(findHitoABoyfriend, Constants.Button.shoot)                .whenHeld(new stage_3(m_Conveyor, m_Wing));
    new JoystickButton(findHitoABoyfriend, Constants.Button.arm)                  .whenHeld(new Arm_motion(m_arm));
    new JoystickButton(findHitoABoyfriend, Constants.Button.aim)                  .whenHeld(new RunCommand(()->m_tower.aimming(),m_tower))
                                                                                  .whenReleased(new InstantCommand(()->m_tower.towerStop(),m_tower))
                                                                                  .whenHeld(new RunCommand(()->m_Racker.PortDistance(),m_Racker))
                                                                                  .whenReleased(new InstantCommand(()->m_Racker.rackerstop(),m_Racker));
    new JoystickButton(findHitoABoyfriend, Constants.Button.tower_left)           .whenHeld(new InstantCommand(()->m_tower.towerForward(),m_tower))
                                                                                  .whenReleased(new InstantCommand(()->m_tower.towerStop(), m_tower));
    new JoystickButton(findHitoABoyfriend, Constants.Button.tower_right)          .whenHeld(new InstantCommand(()->m_tower.towerReverse(), m_tower))
                                                                                  .whenReleased(new InstantCommand(()->m_tower.towerStop(), m_tower));
    new JoystickButton(findHitoABoyfriend, Constants.Button.ranker_up)            .whenHeld(new InstantCommand(()->m_Racker.rackerForward(), m_Racker))
                                                                                  .whenReleased(new InstantCommand(()->m_Racker.rackerstop(), m_Racker));
    new JoystickButton(findHitoABoyfriend, Constants.Button.ranker_down)          .whenHeld(new InstantCommand(()->m_Racker.rackerReverse(), m_Racker))
                                                                                  .whenReleased(new InstantCommand(()->m_Racker.rackerstop(), m_Racker));
  }

  public void Status(){
    Shuffleboard.getTab("Statue").addString("Conveyor", m_Conveyor::getstatus);
    Shuffleboard.getTab("Statue").addString("Wing", m_Wing::getstatus);
    Shuffleboard.getTab("Statue").addString("Flywheel", m_Conveyor::getfly_status);
    Shuffleboard.getTab("Statue").addString("Tower", m_tower::tower_status);
    Shuffleboard.getTab("Statue").addString("Racker", m_Racker::racker_status);
    Shuffleboard.getTab("Statue").addString("Arm", m_arm::arm_status);
    Shuffleboard.getTab("Statue").addBoolean("Racker_limit", m_Racker::rack_limit);
    SmartDashboard.putNumber("tx", m_Limelight.limeldouble()[0]);
    SmartDashboard.putNumber("ty", m_Limelight.limeldouble()[1]);
    SmartDashboard.putNumber("ta", m_Limelight.limeldouble()[2]);
    SmartDashboard.putNumber("distance", m_Limelight.limeldouble()[3]);
  }


  public void Compressor() {
    m_arm.Pneumatic_Status();
  }

  public void teleop(){
    controlDrivetrain.setDefaultCommand(
      new RunCommand(()->
      controlDrivetrain.curvatureDrive(joystick.getY() * 0.4, 
                                       joystick.getZ() * -0.4, 
                                       joystick.getTrigger()), 
        controlDrivetrain)
    );
  }

  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return chooser.getSelected();
  }
}