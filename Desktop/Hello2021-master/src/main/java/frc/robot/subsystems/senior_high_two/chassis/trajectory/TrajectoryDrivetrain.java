/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.senior_high_two.chassis.trajectory;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.Chassis;
import frc.robot.subsystems.senior_high_two.chassis.*;


public class TrajectoryDrivetrain extends DrivetrainBase implements TrajectorySystem{

  private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(getHeading());
  private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Chassis.wheelPitch);
  private Pose2d pose;
  SimpleMotorFeedforward feedForward 
    = new SimpleMotorFeedforward(Chassis.kS, Chassis.kV, Chassis.kA);
  PIDController lpidcontroller 
    = new PIDController(Chassis.kP, 0, 0);
  PIDController rpidcontroller 
    = new PIDController(Chassis.kP, 0, 0);

  /**
   * Creates a new Drivetrain.
   */
  public TrajectoryDrivetrain() {
    Shuffleboard.getTab("Auto").addNumber("x", this::getX);
    Shuffleboard.getTab("Auto").addNumber("y", this::getY);
  }
  /**
     * Provide feedforward controller
     * 
     * @return feedForward controlller 
     */
  public SimpleMotorFeedforward getFeedforward(){
      return feedForward;
  }

  /**
   * Provide PID controller
   * 
   * @return left PID controller
   */
  public PIDController getLeftPidController(){
      return lpidcontroller;
  }

  /**
   * Provide PID controller
   * 
   * @return right PID controller
   */
  public PIDController getRightPidController(){
      return rpidcontroller;
  }
  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  @Override
  public Pose2d getPose() {
    return pose;
  }
  /**
   * encoder velocity to chassis speed
   * 
   * @return current chassis speed
   */
  @Override
  public DifferentialDriveWheelSpeeds getSpeed() {
    return new DifferentialDriveWheelSpeeds(
      leftMas.getSelectedSensorVelocity() * Chassis.distancePerPulse, 
      rightMas.getSelectedSensorVelocity() * Chassis.distancePerPulse
      );
  }

  /**
   * set odmetry,let the starting position 
   * of the robot be the starting point of the trajectory
   * 
   * @param pose2d the trajectory origin
   */
  @Override
  public void setOdometry(Pose2d pose2d){
    odometry.resetPosition(pose2d, pose2d.getRotation());
  }
  
  /**
   * Returns left velocity
   * @return
   */
  @Override
  public double getLeftVelocity(){
    return ((leftMas.getSelectedSensorVelocity()));
  }
  @Override
  public double getLeftPosition(){
    return ((leftMas.getSelectedSensorPosition()));
  }

  /**
   * Returns right velocity
   * @return
   */
  @Override
  public double getRightVelocity(){
    return ((rightMas.getSelectedSensorVelocity()));
  }

  /**
   * Returns right velocity
   * @return
   */
  @Override
  public double getRightPosition(){
    return ((rightMas.getSelectedSensorPosition()));
  }
  
  /**
   * Provide kinematics object, contain track width
   * 
   * @return kinematics
   */
  @Override
  public  DifferentialDriveKinematics getKinematics() {
    return kinematics;
  }

  /**
   * get "X" from odmetry
   * 
   * @return current "X"
   */
  @Override
  public double getX(){
    return odometry.getPoseMeters().getTranslation().getX();
  }  

  /**
   * get "Y" from odmetry
   * 
   * @return current "Y"
   */
  @Override
  public double getY(){
    return odometry.getPoseMeters().getTranslation().getY();
  }
  // @Override
  // public void setOutput(double left, double right) {

  //   leftMas.set(ControlMode.Velocity, left / Motor.distancePerPulse / 10);
  //   rightMas.set(ControlMode.Velocity, right / Motor.distancePerPulse / 10);
  //   SmartDashboard.putNumber("leftOutput ", left / Motor.distancePerPulse / 10);
  //   SmartDashboard.putNumber("rightOutput", right / Motor.distancePerPulse / 10);
  // }
  @Override
  public void voltage(double left, double right){
    double voltage = RobotController.getBatteryVoltage();
    leftMas.set(ControlMode.PercentOutput, left / voltage);
    rightMas.set(ControlMode.PercentOutput, right / voltage);
    SmartDashboard.putNumber("leftOutput ", left / voltage);
    SmartDashboard.putNumber("rightOutput", right / voltage);
  }
 
  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading 
   */
  @Override
  public Rotation2d getHeading() {
    return Rotation2d.fromDegrees(-ahrs.getAngle());
  }

  /**
   * Show message
   */
  @Override
  public void message(){
    //distance
    SmartDashboard.putNumber("leftDistants", getLeftPosition() * Chassis.distancePerPulse);
    SmartDashboard.putNumber("rightDistants", getRightPosition() * Chassis.distancePerPulse);
    SmartDashboard.putNumber("Yaw", ahrs.getYaw());
    SmartDashboard.putNumber("x", getX());
    SmartDashboard.putNumber("Y", getY());
  }

  @Override
  public void periodic() {
    pose = odometry.update(getHeading(), 
    leftMas.getSelectedSensorPosition()  * Chassis.distancePerPulse,
    rightMas.getSelectedSensorPosition() * Chassis.distancePerPulse);
    message();
  }
}
