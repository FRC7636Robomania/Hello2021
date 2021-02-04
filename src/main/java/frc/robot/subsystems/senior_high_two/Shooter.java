/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.senior_high_two;

import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.robot.Constants;
import frc.robot.Constants.Value;

public class Shooter extends SubsystemBase {
  private Limelight limelight;
  private PowerDistributionPanel PDP = new PowerDistributionPanel();
  private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 40, 50, 1);
  private TalonFX flywheelLeft = new TalonFX(Constants.flywheelleft);
  private TalonFX flywheelRight = new TalonFX(Constants.flywheelRight);
  double vel = Value.flywheelSpeed;
  public String status = "stop";

  
  public Shooter(Limelight limelight) {
    // Factory default hardware to prevent unexpected behavior 
    flywheelLeft.configFactoryDefault();
    flywheelRight.configFactoryDefault();
    
    //set sensor
    flywheelLeft.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor,0,Constants.kTimesOut);
    flywheelRight.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, Constants.kTimesOut);
    
    //adjust kP
    flywheelLeft.config_kP(0,Value.fly_kp);
    flywheelRight.config_kP(0,Value.fly_kp);

    //PeakOutput , CurrentLimit , NeutralDeadband 
    flywheelLeft.configPeakOutputForward(1, Constants.kTimesOut);
    flywheelLeft.configPeakOutputReverse(0, Constants.kTimesOut);
    flywheelRight.configPeakOutputForward(1, Constants.kTimesOut);
    flywheelRight.configPeakOutputReverse(0, Constants.kTimesOut);
    flywheelLeft.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
    flywheelRight.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
    //flywheelLeft.configNominalOutputForward(0,10);
    //flywheelRight.configNominalOutputForward(0,10);
    flywheelLeft.setNeutralMode(NeutralMode.Coast);
    flywheelRight.setNeutralMode(NeutralMode.Coast);
    flywheelLeft.configNeutralDeadband(0.005,Constants.kTimesOut);
    flywheelRight.configNeutralDeadband(0.005,Constants.kTimesOut);
    
    //Closedloop,Openedloop
    flywheelLeft.configClosedloopRamp(1, Constants.kTimesOut);
    flywheelRight.configClosedloopRamp(1, Constants.kTimesOut);
    
    //InvertType
    flywheelRight.follow(flywheelLeft);
    flywheelLeft.setInverted(false);
    flywheelRight.setInverted(InvertType.OpposeMaster);

    //test mode
    //flywheelLeft.configVoltageCompSaturation(11);
    //flywheelRight.configVoltageCompSaturation(11);
    flywheelLeft.configVoltageMeasurementFilter(10);
    flywheelRight.configVoltageMeasurementFilter(10);
    
    //flywheelLeft.enableVoltageCompensation(false);
    //flywheelRight.enableVoltageCompensation(false);

  }

  public void forward() {
    flywheelRight.follow(flywheelLeft);
    if(limelight.limeldouble()[3]<=200){
      flywheelLeft.config_kF(0, 0.05);
      flywheelRight.config_kF(0, 0.05);
      flywheelLeft.set(ControlMode.Velocity, 11000);
    }else{
      flywheelLeft.config_kF(0, 0.06);
      flywheelRight.config_kF(0, 0.06);
      flywheelLeft.set(ControlMode.Velocity,12000);
    }
  }


  public void stop() {
    flywheelLeft.set(ControlMode.PercentOutput,0);
  }

  public void percentage(){
    flywheelLeft.set(ControlMode.PercentOutput, 1);
  }

  public double getflywheelVelocity(){ 
    return flywheelLeft.getSelectedSensorVelocity();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("flyvel", flywheelLeft.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("flyvel2", flywheelLeft.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("fly_current", PDP.getCurrent(Constants.flywheelleft_PDP));
    SmartDashboard.putNumber("fly_Voltage", flywheelRight.getMotorOutputVoltage());
  }
}
