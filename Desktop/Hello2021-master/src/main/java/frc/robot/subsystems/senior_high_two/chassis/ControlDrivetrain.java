// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.senior_high_two.chassis;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpiutil.math.MathUtil;

public class ControlDrivetrain extends DrivetrainBase {
  /** Creates a new ControlDrivetrain. */
  /** curvature drive */
  private double m_quickStopAccumulator = 0, leftout = 0, rightout = 0;
  public static double rate = 1.0d;
  public ControlDrivetrain() {

  }
  public void changeRate(){
    double another = 0.5d;
    if(rate > another){
      rate = another;
    }else{
      rate = 1.0d;
    }
  }
  public double applyDeadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn, boolean isFast){
    xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);
    if(xSpeed < 0.05 & xSpeed > -0.05){
      xSpeed = 0;
    }//簡易死區設定
    if(Math.abs(zRotation)<0.05){
      zRotation =0;
     }
    zRotation = MathUtil.clamp(zRotation, -1.0, 1.0);
    double angularPower;
    boolean overPower;
    double m_quickStopAlpha = 0.1;
    if (isQuickTurn) {
      if (Math.abs(xSpeed) < 0.1) {
        m_quickStopAccumulator = (1 - 0.1) * m_quickStopAccumulator
            + m_quickStopAlpha * MathUtil.clamp(zRotation, -1.0, 1.0) * 2;
      }

      overPower = true;
      angularPower = zRotation;
    } else {
      overPower = false;
      angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

      if (m_quickStopAccumulator > 1) {
        m_quickStopAccumulator -= 1;
      } else if (m_quickStopAccumulator < -1) {
        m_quickStopAccumulator += 1;
      } else {
        m_quickStopAccumulator = 0.0;
      }
    }

    double leftMotorOutput = xSpeed - angularPower;
    double rightMotorOutput = xSpeed + angularPower;

    // If rotation is overpowered, reduce both outputs to within acceptable range
    if (overPower) {
      if (leftMotorOutput > 1.0) {
        rightMotorOutput -= leftMotorOutput - 1.0;
        leftMotorOutput = 1.0;
      } else if (rightMotorOutput > 1.0) {
        leftMotorOutput -= rightMotorOutput - 1.0;
        rightMotorOutput = 1.0;
      } else if (leftMotorOutput < -1.0) {
        rightMotorOutput -= leftMotorOutput + 1.0;
        leftMotorOutput = -1.0;
      } else if (rightMotorOutput < -1.0) {
        leftMotorOutput -= rightMotorOutput + 1.0;
        rightMotorOutput = -1.0;
      }
    }

    // Normalize the wheel speeds
    double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
    if (maxMagnitude > 1.0) {
      leftMotorOutput /= maxMagnitude;
      rightMotorOutput /= maxMagnitude;
    }
    leftout=  leftMotorOutput;
    rightout = rightMotorOutput;
    leftMas.set(ControlMode.PercentOutput, -leftout * rate * (isFast ? 1.5 : 1));
    rightMas.set(ControlMode.PercentOutput, -rightout * rate * (isFast ? 1.5 : 1));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
