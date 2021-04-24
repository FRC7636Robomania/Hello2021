/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.senior_high_two.chassis;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.MotorFactory;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants.*;


/**
 * Must be sure these objects will be instantiated only once
 */
public class DrivetrainBase extends SubsystemBase {
  protected static WPI_TalonFX leftMas  = new WPI_TalonFX(Chassis.leftMaster);
  protected static WPI_TalonFX leftFol  = new WPI_TalonFX(Chassis.leftFollewer);
  protected static WPI_TalonFX rightMas = new WPI_TalonFX(Chassis.rightMaster);
  protected static WPI_TalonFX rightFol = new WPI_TalonFX(Chassis.rightFollower);
  protected static AHRS ahrs = new AHRS(SPI.Port.kMXP);
  private   static boolean isFirst = true; 
  private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 40, 50, 1);

  /**
   * Creates a new DrivetrainBase.
   */
  public DrivetrainBase() {
    if(isFirst){
      firstConfig();
      isFirst = false;
    }
  }

  public void firstConfig(){
    MotorFactory.setFollower(leftMas, leftFol);
    MotorFactory.setInvert(leftMas, Chassis.isLeftMotorInvert);
    MotorFactory.setPosion(leftMas, 0, 0, 10);
    MotorFactory.setSensor(leftMas, FeedbackDevice.IntegratedSensor);
    MotorFactory.setSensorPhase(leftMas, Chassis.isLeftPhaseInvert);
    MotorFactory.configLikePrevious(leftFol, Chassis.isLeftPhaseInvert, Chassis.isLeftMotorInvert);
    leftMas.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);

    MotorFactory.setFollower(rightMas, rightFol);
    rightMas.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
    MotorFactory.configLikePrevious(rightMas, Chassis.isRightPhaseInvert, Chassis.isRightMotorInvert);
    MotorFactory.configLikePrevious(rightFol, Chassis.isRightPhaseInvert, Chassis.isRightMotorInvert);
    MotorFactory.voltageCompSaturation(rightMas, 11);
    MotorFactory.voltageCompSaturation(leftMas,  11);

    MotorFactory.configPF(leftMas,  0, 0, 0);
    MotorFactory.configPF(rightMas, 0, 0, 0);
    leftFol.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 255);
    rightFol.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 255);
    leftMas.setNeutralMode(NeutralMode.Coast);
    rightMas.setNeutralMode(NeutralMode.Coast);
    
    ahrs.reset();
  }
  public boolean isMove(){
    if(Math.abs(leftMas.getSelectedSensorVelocity()) > 21600 * 0.1){
      return true;
    }
    return false;
  }
  public void resetSensor(){
    MotorFactory.setPosion(leftMas, 0, 0, 0);
    MotorFactory.setPosion(rightMas, 0, 0, 0);
    MotorFactory.setPosion(leftFol, 0, 0, 0);
    MotorFactory.setPosion(rightFol, 0, 0, 0);
    ahrs.reset();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("left", leftMas.getSelectedSensorVelocity());
    SmartDashboard.putNumber("right", rightMas.getSelectedSensorVelocity());
    // This method will be called once per scheduler run
  }
}
