/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.senior_high_two;

import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.robot.Constants;
import frc.robot.Constants.Value;

public class Shooter extends SubsystemBase {
  private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 40, 50, 0.8);
  private TalonFX flywheelLeft = new TalonFX(Constants.flywheelleft);
  private TalonFX flywheelRight = new TalonFX(Constants.flywheelRight);
  
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
    flywheelLeft.config_kD(0, Value.fly_kD);
    flywheelRight.config_kD(0, Value.fly_kD);
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
    flywheelLeft.configVoltageMeasurementFilter(11);
    flywheelRight.configVoltageMeasurementFilter(11);
    
    //flywheelLeft.enableVoltageCompensation(false);
    //flywheelRight.enableVoltageCompensation(false);

  }

  public void forward() {
    flywheelRight.follow(flywheelLeft);
    if(Limelight.getdistances()<=80){
      flywheelLeft.config_kF(0, 0.05);
      flywheelRight.config_kF(0, 0.05);
      flywheelLeft.set(ControlMode.Velocity, 8000);
    }
    else if(Limelight.getdistances() <= 119) {
      flywheelLeft.config_kF(0, Constants.Value.fly_kFCLOSE);
      flywheelRight.config_kF(0, Constants.Value.fly_kFCLOSE);
      flywheelLeft.set(ControlMode.Velocity, Constants.Value.fly_speedCLOSE);
    }else if(Limelight.getdistances() <=140){
      flywheelLeft.config_kF(0, Constants.Value.fly_kFAR);
      flywheelRight.config_kF(0, Constants.Value.fly_kFAR);
      flywheelLeft.set(ControlMode.Velocity, Constants.Value.fly_speedFAR);
    }// }else{
    //   flywheelLeft.config_kF(0, Constants.Value.fly_kFAR);
    //   flywheelRight.config_kF(0, Constants.Value.fly_kFAR);
    //   flywheelLeft.set(ControlMode.Velocity, 12500);
    // }
      //else{
    //   flywheelLeft.config_kF(0, 0.05);
    //   flywheelRight.config_kF(0, 0.05);
    //   flywheelLeft.set(ControlMode.Velocity, 11800);
    // }
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
    SmartDashboard.putNumber("fly_Voltage", flywheelRight.getMotorOutputVoltage());
    SmartDashboard.putNumber("fly_current", PDP.getCurrent(12));
  }
}
