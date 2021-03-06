/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.senior_high_one;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import java.nio.file.FileAlreadyExistsException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.senior_high_two.Limelight;
import frc.robot.subsystems.senior_high_two.PDP;
import frc.robot.subsystems.senior_high_two.Shooter;
import frc.robot.Constants;


public class Conveyor extends SubsystemBase {
  private WPI_VictorSPX Conveyor = new WPI_VictorSPX(Constants.conveyor);
  private Shooter shooter;
  private String status = "stop",fly_status = "No working";
  
  public Conveyor(Shooter Shooter) {
    this.shooter = Shooter;
    Conveyor.configNeutralDeadband(0.1);
    Conveyor.configPeakOutputForward(0.8);
    Conveyor.configPeakOutputReverse(-0.8);
    Conveyor.setNeutralMode(NeutralMode.Coast);
    Conveyor.setInverted(true);
  }

  public String getstatus(){
    return status;
  }

  public String getfly_status(){
    return fly_status;
  }

  public void forward(){
    double limit = 0;
    if(Limelight.getdistances() < 80){
      limit = 7700;
    }else if (Limelight.getdistances() < 130){
      limit = 9500;
    }else {
      limit = 11000;
    }
    if(shooter.getflywheelVelocity()>=limit){
        Conveyor.set(ControlMode. PercentOutput , 0.5 );
        fly_status = "Normal RPM";
      }else{
        Conveyor.set(ControlMode. PercentOutput , 0 );
        fly_status = "Slowly RPM";
      }
    status = "Engage!!!";
  }

  public void stop(){
    Conveyor.set(ControlMode.PercentOutput,0);
    status = "stop！";
  }

  public void reverse(){
    if (PDP.getCurrent(Constants.Conveyor_PDP)>25){
      Conveyor.set(ControlMode.PercentOutput,0);
    }else{
      Conveyor.set(ControlMode.PercentOutput,-0.5);
    }
    status = "Retreat!!!";
  }
  @Override
    public void periodic(){
      if(PDP.getCurrent(Constants.Conveyor_PDP)>25){
        Conveyor.set(ControlMode.PercentOutput,0);
      }
      
      
      
    }
  }