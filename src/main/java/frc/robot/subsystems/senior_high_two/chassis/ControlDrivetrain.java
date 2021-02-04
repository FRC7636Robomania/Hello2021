// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.senior_high_two.chassis;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpiutil.math.MathUtil;

public class ControlDrivetrain extends DrivetrainBase {
  /** Creates a new ControlDrivetrain. */
  /** curvature drive */
  private double m_quickStopAccumulator = 0, leftout = 0, rightout = 0, m_deadband = 0.02;

  public ControlDrivetrain() {

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

  private boolean squareInputs = true;

  public void arcadeDrive(double xSpeed, double zRotation){
    xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);
    xSpeed = applyDeadband(xSpeed, m_deadband);

    zRotation = MathUtil.clamp(zRotation, -1.0, 1.0);
    zRotation = applyDeadband(zRotation, m_deadband);

    // Square the inputs (while preserving the sign) to increase fine control
    // while permitting full power.
    if (squareInputs) {
      xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
      zRotation = Math.copySign(zRotation * zRotation, zRotation);
    }

    double leftMotorOutput;
    double rightMotorOutput;

    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

    if (xSpeed >= 0.0) {
      // First quadrant, else second quadrant
      if (zRotation >= 0.0) {
        leftMotorOutput = maxInput;
        rightMotorOutput = xSpeed - zRotation;
      } else {
        leftMotorOutput = xSpeed + zRotation;
        rightMotorOutput = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (zRotation >= 0.0) {
        leftMotorOutput = xSpeed + zRotation;
        rightMotorOutput = maxInput;
      } else {
        leftMotorOutput = maxInput;
        rightMotorOutput = xSpeed - zRotation;
      }
    }
    leftMas.set(MathUtil.clamp(-leftMotorOutput, -1.0, 1.0));
    rightMas.set(MathUtil.clamp(-rightMotorOutput, -1.0, 1.0));
  }

  public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn){
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

    leftMas.set(ControlMode.PercentOutput, -leftout);
    rightMas.set(ControlMode.PercentOutput, -rightout);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
